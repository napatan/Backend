package com.taekwondogym.backend.service;

import com.taekwondogym.backend.model.Cart;
import com.taekwondogym.backend.model.CartItem;
import com.taekwondogym.backend.model.Product;
import com.taekwondogym.backend.model.User;
import com.taekwondogym.backend.repository.CartRepository;
import com.taekwondogym.backend.repository.ProductRepository;
import com.taekwondogym.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get or create a cart for a given email
    public Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null; // User not found
        }
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user); // Associate the cart with the user
                    return cartRepository.save(newCart);
                });
    }

 // Add product to cart by email, check stock before adding
    public Cart addProductToCart(String email, Long productId, int quantity) {
        Cart cart = getOrCreateCart(email);
        if (cart == null) {
            return null; // Handle case where user is not found
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (!product.isAvailable()) {
                return null; // Do not add unavailable products
            }

            if (product.getStock() < quantity) {
                return null; // Insufficient stock
            }

            CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity); // Update quantity
            } else {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                newItem.setCart(cart);
                cart.getItems().add(newItem);
            }
            return cartRepository.save(cart);
        }
        return null; // Handle product not found
    }



    // Remove product from cart by email
    public void removeProductFromCart(String email, Long productId) {
        Cart cart = getOrCreateCart(email);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepository.save(cart);
        }
    }

    // Get cart items by email
    public List<CartItem> getCartItems(String email) {
        Cart cart = getOrCreateCart(email);
        if (cart != null) {
            // Filter out unavailable products
            return cart.getItems().stream()
                .filter(item -> item.getProduct().isAvailable())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    // Update quantity in cart by email
    public void updateQuantityInCart(String email, Long productId, int quantity) {
        Cart cart = getOrCreateCart(email);
        if (cart != null) {
            Optional<CartItem> itemOptional = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (itemOptional.isPresent()) {
                CartItem item = itemOptional.get();
                item.setQuantity(quantity); // Update the quantity
                cartRepository.save(cart); // Save the updated cart
            }
        }
    }

    // Clear cart by email
    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        if (cart != null) {
            cart.getItems().clear(); // Clear the items
            cartRepository.save(cart); // Save the updated cart
        }
    }
    
    public void removeUnavailableProductsFromCart() {
        List<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            List<CartItem> itemsToRemove = cart.getItems().stream()
                .filter(item -> !item.getProduct().isAvailable())
                .collect(Collectors.toList());

            cart.getItems().removeAll(itemsToRemove); // Remove unavailable items
            cartRepository.save(cart); // Save the updated cart
        }
    }

}
