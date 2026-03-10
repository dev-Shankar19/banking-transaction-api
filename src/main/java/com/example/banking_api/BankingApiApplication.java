package com.example.banking_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApiApplication {

	public static void main(String[] args) {
		System.out.println(com.example.bankingapi.util.JwtUtil.generateToken("john"));

		SpringApplication.run(BankingApiApplication.class, args);

	}

}
