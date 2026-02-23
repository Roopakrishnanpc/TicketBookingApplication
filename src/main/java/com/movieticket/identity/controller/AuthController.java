package com.movieticket.identity.controller;

import com.movieticket.identity.dto.RegisterRequest;
import com.movieticket.identity.dto.TokenRequest;
import com.movieticket.identity.dto.TokenResponse;
import com.movieticket.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        authService.register(req);
    }

    @PostMapping("/token")
    public TokenResponse token(@RequestBody TokenRequest req) {
        return authService.generateToken(req);
    }
}