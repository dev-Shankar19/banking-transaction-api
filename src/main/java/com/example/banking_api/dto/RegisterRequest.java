package com.example.banking_api.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 20,message = "Username too long")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}