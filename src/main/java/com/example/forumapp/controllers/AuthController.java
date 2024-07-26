package com.example.forumapp.controllers;

import com.example.forumapp.config.JwtAuthenticationFilter;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.models.requests.AccountActivationRequest;
import com.example.forumapp.models.requests.LoginRequest;
import com.example.forumapp.models.requests.RegisterRequest;
import com.example.forumapp.models.responses.LoginResponse;
import com.example.forumapp.services.AuthenticationService;
import com.example.forumapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<User> login(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/activate")
    public ResponseEntity<LoginResponse> activateAccount(@RequestBody AccountActivationRequest req){
        if(authenticationService.activateAccount(req)){
            logger.info("usao u if");
            return ResponseEntity.ok(userService.login(req.getUsername()));
        }
        return null;
    }
}