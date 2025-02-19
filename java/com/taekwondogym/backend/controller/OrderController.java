package com.taekwondogym.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.taekwondogym.backend.model.CartItem;
import com.taekwondogym.backend.model.Order;
import com.taekwondogym.backend.model.OrderItem;
import com.taekwondogym.backend.service.CartService;
import com.taekwondogym.backend.service.OrderService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam String email, 
                                          @RequestParam String name,
                                          @RequestParam String surname,
                                          @RequestParam String telephone,
                                          @RequestParam String deliveryType,
                                          @RequestParam(required = false) String homeAddress,
                                          @RequestParam(required = false) String postcode,
                                          @RequestParam(required = false) String city,
                                          @RequestParam(required = false) String district,
                                          @RequestParam(required = false) String subDistrict,
                                          @RequestParam(required = false) String road,
                                          @RequestParam(required = false) String soi,
                                          @RequestParam(required = false) String moo,
                                          @RequestParam("receiptImage") MultipartFile receiptImage) {
        
        try {
            List<CartItem> cartItems = cartService.getCartItems(email); // Retrieve cart items
            double totalAmount = cartItems.stream()
                                           .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                                           .sum();

            Order order = new Order();
            order.setName(name);
            order.setSurname(surname);
            order.setTelephone(telephone);
            order.setDeliveryType(deliveryType);
            order.setTotalAmount(totalAmount);
            order.setOrderDate(LocalDateTime.now());

            // Set address fields if delivery is shipping
            if ("shipping".equalsIgnoreCase(deliveryType)) {
                order.setHomeAddress(homeAddress);
                order.setPostcode(postcode);
                order.setCity(city);
                order.setDistrict(district);
                order.setSubDistrict(subDistrict);
                order.setRoad(road);
                order.setSoi(soi);
                order.setMoo(moo);
            }

            // Handle receipt file upload and set the path in order
            String receiptFilePath = orderService.saveReceiptImage(receiptImage);
            order.setReceiptFilePath(receiptFilePath);

            // Create order items and add to order
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                orderItem.setOrder(order);
                order.getOrderItems().add(orderItem); // Add to the order items list
            }

            // Call service to save order
            orderService.createOrder(order, email); // Pass email instead of username
            cartService.clearCart(email); // Clear the cart after order is created
            return ResponseEntity.ok().build();
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to save the receipt image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                                 .body("Failed to create order: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        Optional<Order> order = orderService.findById(orderId);
        return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Order>> getAllOrdersByEmail(@PathVariable String email) { // Changed from username to email
        List<Order> orders = orderService.findAllByEmail(email); // Pass email instead of username
        return ResponseEntity.ok(orders);
    }

    // Endpoint for admins to get all orders
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }
}