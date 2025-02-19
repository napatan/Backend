package com.taekwondogym.backend.service;

import com.taekwondogym.backend.model.Product;
import com.taekwondogym.backend.repository.ProductRepository;
import com.taekwondogym.backend.store.ProductImageStore;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageStore productImageStore;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailable(true); // Fetch only available products
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product addProduct(Product product, List<MultipartFile> images) throws IOException {
        if (images != null && !images.isEmpty()) {
            List<String> imagePaths = saveImages(images); // Save images and get their paths
            product.setImagePaths(imagePaths); // Set the image paths in the product
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails, List<MultipartFile> images) throws IOException {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            // Update fields
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setAvailable(productDetails.isAvailable()); // Update availability status
            product.setStock(productDetails.getStock()); // Update stock

            // If new images are uploaded, process them
            if (images != null && !images.isEmpty()) {
                // Delete old images
                for (String imagePath : product.getImagePaths()) {
                    deleteImage(imagePath); // Delete old images from storage
                }

                // Save new images and update the image paths
                List<String> imagePaths = saveImages(images);
                product.setImagePaths(imagePaths); // Update the product with new image paths
            }

            // Save the updated product
            return productRepository.save(product);
        }
        return null; // Return null if the product is not found
    }

    public Product toggleProductAvailability(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setAvailable(!product.isAvailable()); // Toggle availability
            return productRepository.save(product);
        }
        return null; // Product not found
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            // Delete associated images before deleting the product
            for (String imagePath : product.getImagePaths()) {
                deleteImage(imagePath); // Delete each image associated with the product
            }
            productRepository.deleteById(id); // Delete the product from the database
        }
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByStockLessThanEqual(threshold); // Fetch products with low stock
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    productImageStore.setContent(filename, image.getInputStream()); // Save the image using the store
                    imagePaths.add(filename); // Add the filename to the list
                }
            }
        }
        return imagePaths; // Return the list of saved image paths
    }

    private void deleteImage(String imagePath) {
        try {
            productImageStore.delete(imagePath);  // Delete the image from storage
        } catch (IOException e) {
            e.printStackTrace();  // Handle exceptions properly (consider logging)
        }
    }
}
