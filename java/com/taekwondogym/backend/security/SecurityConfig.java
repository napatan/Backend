package com.taekwondogym.backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:4200")); // Frontend origin
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);	
            return config;
        }))
        .csrf(csrf -> csrf
                .disable())
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/", "/api/users/register", "/api/users/login").permitAll()
                    .requestMatchers("/css/**", "/js/**").permitAll()
                    .requestMatchers("/login", "/register").permitAll()
                    .requestMatchers("/prod", "/shop").hasRole("ADMIN")
                    .requestMatchers("/uploads/images/**","/uploads/**").permitAll()
                    // Protect the trainer related pages and allow only authorized users
                    .requestMatchers(HttpMethod.GET, "/api/trainers/").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/trainers/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/trainers/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/trainers/**").hasRole("ADMIN")
                    
                    // Allow unauthenticated access to TrainingClass GET methods (for viewing)
                    .requestMatchers(HttpMethod.GET, "/api/classes/**").permitAll()
                    
                    // Allow only admins to create, update, or delete TrainingClass
                    .requestMatchers(HttpMethod.POST, "/api/classes/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/classes/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/classes/**").hasRole("ADMIN")
                    
                    // Product-related permissions
                    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                    
                    // Cart-related permissions
                    .requestMatchers("/cart", "/api/cart/**").authenticated() // Changed to `/api/cart/**` as per REST convention
                    .requestMatchers(HttpMethod.GET, "/api/products/{id}/check-stock").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/cart/products/**").authenticated() // Allow users to update quantities
                    .requestMatchers(HttpMethod.PUT, "/api/cart/products/**").authenticated() // Allow users to update quantities
                    .requestMatchers(HttpMethod.DELETE, "/api/cart/products/**").authenticated() // Allow users to remove products

                    // Achievement-related permissions
                    //.requestMatchers("/api/achievements/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/achievements","/api/achievements/","/api/achievements/{id}").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/achievements/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/achievements/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/achievements/**").hasRole("ADMIN")

                    // Member-related permissions
                    .requestMatchers("/api/members/current").authenticated()  // Users can access and submit their own forms
                    .requestMatchers("/api/members/**").hasRole("ADMIN")      // Admin can access any member form
                    .requestMatchers(HttpMethod.GET, "/api/members/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/members/{id}").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/orders/**").authenticated()  // Allow authenticated users to place orders
                    .requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole("ADMIN")  // Admins can view orders
                    .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}