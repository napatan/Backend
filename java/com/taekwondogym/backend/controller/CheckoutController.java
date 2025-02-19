package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String bankId, @RequestParam("receipt") MultipartFile receipt) {
        try {
            checkoutService.processCheckout(bankId, receipt);
            return ResponseEntity.ok("Checkout processed successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing checkout.");
        }
    }
}
