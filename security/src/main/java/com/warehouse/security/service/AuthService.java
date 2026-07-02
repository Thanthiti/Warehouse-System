package com.warehouse.security.service;

import com.warehouse.entity.domain.Users;
import com.warehouse.repository.repo.UserRepositoty;
import com.warehouse.security.dto.AuthResponse;
import com.warehouse.security.dto.LoginRequest;
import com.warehouse.security.dto.RegisterRequest;
import com.warehouse.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoty userRepositoty;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (userRepositoty.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered: " + email);
        }

        Users user = new Users();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole((request.getRole() == null || request.getRole().isBlank())
                ? "USER" : request.getRole().toUpperCase(Locale.ROOT));
        user.setStatusCode("ACTIVE");
        user.setCreateDate(new Date());
        user.setCreateBy(user.getEmail());

        Users saved;
        try {
            saved = userRepositoty.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Email is already registered: " + email);
        }

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole());
        return new AuthResponse(token, saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        Users user = userRepositoty.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }

    private String normalizeEmail(String rawEmail) {
        return rawEmail.trim().toLowerCase(Locale.ROOT);
    }
}
