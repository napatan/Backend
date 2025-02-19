package com.taekwondogym.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taekwondogym.backend.model.CartItem;
import com.taekwondogym.backend.model.Order;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private CartService cartService;

    public void processCheckout(String bankId, MultipartFile receipt) throws IOException {
        // Handle the bank ID (e.g., store in database, process payment logic, etc.)
        
        // Get the current user's cart
        String username = getCurrentUsername(); // Implement this method to get the current username

        // Get cart items to calculate total
        List<CartItem> cartItems = cartService.getCartItems(username);
        double totalAmount = calculateTotal(cartItems);

        // Save the receipt file locally (you may want to save it to a specific directory)
        if (!receipt.isEmpty()) {
            String fileName = receipt.getOriginalFilename();
            File file = new File("path/to/save/" + fileName); // Specify your directory
            receipt.transferTo(file); // Save the file
        }

        // Implement logic to save order with totalAmount and other details
        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setUsername(username); // Set other order details
        // Save the order to the database (you need an OrderRepository for this)
        // orderRepository.save(order);

        // Clear the cart after processing the order
        cartService.clearCart(username); // This should now work without errors
    }

    private double calculateTotal(List<CartItem> items) {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity(); // Assuming Product has a getPrice method
        }
        return total;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }
}
