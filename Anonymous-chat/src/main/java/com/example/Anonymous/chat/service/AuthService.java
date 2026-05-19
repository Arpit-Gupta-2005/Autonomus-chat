package com.example.Anonymous.chat.service;

import com.example.Anonymous.chat.dto.request.LoginRequest;
import com.example.Anonymous.chat.dto.request.RegisterRequest;
import com.example.Anonymous.chat.dto.response.AuthResponse;
import com.example.Anonymous.chat.model.User;
import com.example.Anonymous.chat.repository.UserRepository;
import com.example.Anonymous.chat.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByDisplayName(request.getDisplayName())) {
            throw new RuntimeException("Display name already taken");
        }

        User user = User.builder()
                .displayName(request.getDisplayName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .totalChats(0)
                .createdAt(LocalDateTime.now())
                .lastSeen(LocalDateTime.now())
                .isActive(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        log.info("User logged in: {}", user.getEmail());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
