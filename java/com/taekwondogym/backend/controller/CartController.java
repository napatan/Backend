package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.dto.CartItemDTO;
import com.taekwondogym.backend.model.Cart;
import com.taekwondogym.backend.model.CartItem;
import com.taekwondogym.backend.model.Product;
import com.taekwondogym.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Method to get the current user's username
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null; // Return null if user is not authenticated
    }

    // Get the current user's cart
    @GetMapping
    public ResponseEntity<Cart> getCart() {
        String username = getCurrentUsername();
        if (username != null) {
            Cart cart = cartService.getOrCreateCart(username);
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // Add product to the cart with quantity
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable Long productId, @RequestParam int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
        }
        String username = getCurrentUsername();
        if (username != null) {
            Cart updatedCart = cartService.addProductToCart(username, productId, quantity);  
            if (updatedCart != null) {
                return ResponseEntity.ok(updatedCart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
    }

    // Remove product from the cart
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable Long productId) {
        String username = getCurrentUsername();
        if (username != null) {
            cartService.removeProductFromCart(username, productId);
            return ResponseEntity.ok("Product removed from cart.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
    }

    // Get all cart items
    @GetMapping("/products")
    public ResponseEntity<List<CartItem>> getProductsInCart() {
        String username = getCurrentUsername();
        if (username != null) {
            List<CartItem> cartItems = cartService.getCartItems(username);
            return ResponseEntity.ok(cartItems);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // Update product quantity in the cart
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long productId, @RequestBody CartItemDTO cartItemDTO) {
        String username = getCurrentUsername();
        if (username != null) {
            if (cartItemDTO.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
            }
            cartService.updateQuantityInCart(username, productId, cartItemDTO.getQuantity());
            return ResponseEntity.ok("Product quantity updated.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
    }
}
