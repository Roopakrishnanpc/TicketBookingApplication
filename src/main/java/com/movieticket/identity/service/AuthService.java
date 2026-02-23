package com.movieticket.identity.service;

import com.movieticket.identity.domain.*;
import com.movieticket.identity.dto.RegisterRequest;
import com.movieticket.identity.dto.TokenRequest;
import com.movieticket.identity.dto.TokenResponse;
import com.movieticket.identity.repository.*;
import com.movieticket.identity.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void register(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        if (userRepository.findByUsername(req.getUsername()).isPresent())
            throw new RuntimeException("Username already exists");

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(Role.valueOf(req.getRole()))
                .active(true)
                .build();

        userRepository.save(user);
    }

    public TokenResponse generateToken(TokenRequest req) {

        if ("password".equals(req.getGrantType())) {

            User user = userRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if (!encoder.matches(req.getPassword(), user.getPassword()))
                throw new RuntimeException("Invalid credentials");

            String access = jwtService.generateAccessToken(user);
            String refresh = jwtService.generateRefreshToken(user);

            refreshRepo.deleteByUsername(user.getUsername());

            refreshRepo.save(
                    RefreshToken.builder()
                            .token(refresh)
                            .username(user.getUsername())
                            .expiryDate(Instant.now().plusSeconds(604800))
                            .build()
            );

            return new TokenResponse(access, refresh, "Bearer");
        }

        if ("refresh_token".equals(req.getGrantType())) {

            RefreshToken token = refreshRepo.findByToken(req.getRefreshToken())
                    .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

            if (token.getExpiryDate().isBefore(Instant.now()))
                throw new RuntimeException("Refresh token expired");

            User user = userRepository.findByUsername(token.getUsername())
                    .orElseThrow();

            String newAccess = jwtService.generateAccessToken(user);

            return new TokenResponse(newAccess, req.getRefreshToken(), "Bearer");
        }

        throw new RuntimeException("Unsupported grant type");
    }
}