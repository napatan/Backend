package com.taekwondogym.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping("/")
    public String showHomePage() {
        return "index"; 
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; 
    }

    @GetMapping("/achievements")
    public String showAchievementsPage() {
        return "achievements"; 
    }

    @GetMapping("/shop")
    public String showShopPage() {
        return "shop"; 
    }
    
    @GetMapping("/prod")
    public String showProduct() {
        return "prodman"; 
    }
    
    @GetMapping("/cart")
    public String showCart() {
        return "cart"; 
    }
    
    @GetMapping("/member")
    public String showMember() {
        return "member"; 
    }
    
    @GetMapping("/checkout")
    public String showCheckout() {
        return "checkout"; 
    }
    @GetMapping("/order")
    public String showOrder() {
        return "order"; 
    }
    @GetMapping("/classes")
    public String showClasses() {
    	return "TrainingClass";
    }
    @GetMapping("/trainer")
    public String showTrainer() {
        return "trainer"; 
    }
    @GetMapping("/train")
    public String showTrain() {
        return "train"; 
    }
    @GetMapping("/order-preview/{orderId}")
    public String showOrderPreview(@PathVariable Long orderId, Model model) {
        // You can fetch the order by orderId from the database and add it to the model if needed
        model.addAttribute("orderId", orderId);
        return "order-preview"; // Return the order-preview.html template
    }
    
}
