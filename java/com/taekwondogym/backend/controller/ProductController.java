package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.model.Product;
import com.taekwondogym.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/available")
    public List<Product> getAvailableProducts() {
        return productService.getAvailableProducts(); // Fetch only available products
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(@RequestParam int threshold) {
        return productService.getLowStockProducts(threshold); // Fetch products with low stock
    }

    // New endpoint for checking stock availability
    @GetMapping("/{id}/check-stock")
    public ResponseEntity<StockCheckResponse> checkStock(@PathVariable Long id, @RequestParam int quantity) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        boolean available = product.getStock() >= quantity;
        return ResponseEntity.ok(new StockCheckResponse(available, product.getName()));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") Product product,
            @RequestPart(required = false, value = "images") List<MultipartFile> images) throws IOException {
        Product savedProduct = productService.addProduct(product, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product productDetails,
            @RequestPart(required = false, value = "images") List<MultipartFile> images) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails, images);
            return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Product> toggleProductAvailability(@PathVariable Long id) {
        Product updatedProduct = productService.toggleProductAvailability(id);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // Response DTO for stock check
    public static class StockCheckResponse {
        private boolean available;
        private String productName;

        public StockCheckResponse(boolean available, String productName) {
            this.available = available;
            this.productName = productName;
        }

        public boolean isAvailable() {
            return available;
        }

        public String getProductName() {
            return productName;
        }
    }
}
