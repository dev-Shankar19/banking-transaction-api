package com.example.banking_api.service;


import com.example.banking_api.dto.LoginRequest;
import com.example.banking_api.dto.LoginResponse;
import com.example.banking_api.dto.RegisterRequest;
import com.example.banking_api.entity.User;
import com.example.banking_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request){
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        String token = com.example.bankingapi.util.JwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {



        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}
