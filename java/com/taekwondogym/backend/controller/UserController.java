package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.dto.LoginRequest;
import com.taekwondogym.backend.model.User;
import com.taekwondogym.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), // Changed from username to email
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                 SecurityContextHolder.getContext());

            return ResponseEntity.ok(new LoginResponse("Login successful", loginRequest.getEmail())); // Changed to email
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new LoginResponse("Invalid email or password", null)); // Changed from username to email
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new LoginResponse("An error occurred during login", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new LoginResponse("Logout successful", null));
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> currentUser(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(principal.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
}

class LoginResponse {
    private String message;
    private String email; // Changed from username to email

    public LoginResponse(String message, String email) { // Constructor updated to email
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() { // Changed from getUsername to getEmail
        return email;
    }

    public void setEmail(String email) { // Changed from setUsername to setEmail
        this.email = email;
    }
}
