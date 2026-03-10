package com.example.banking_api.repository;

import com.example.banking_api.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String usernaem);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
