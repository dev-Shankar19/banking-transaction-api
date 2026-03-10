
# Banking Transaction API

A secure banking backend built using Spring Boot that supports account management and money transfers with ACID guarantees.

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* PostgreSQL
* JPA / Hibernate
* Swagger (OpenAPI)

## Features

* User Registration & Login
* JWT Authentication
* Account Creation
* Deposit & Withdraw APIs
* Secure Money Transfer
* ACID Transaction Handling
* Idempotent Transfer Protection
* Optimistic Locking for Concurrency
* Paginated Transaction History

## API Endpoints

Auth
POST /auth/register
POST /auth/login

Accounts
POST /accounts
GET /accounts/{id}

Transactions
POST /deposit
POST /withdraw
POST /transfer

History
GET /transactions?accountId=1&page=0&size=20

## Run Locally

Clone the repository

git clone https://github.com/dev-Shankar19/banking-transaction-api.git

Start PostgreSQL and create database

CREATE DATABASE banking_db;

Update application.properties with DB credentials

Run the application

mvn spring-boot:run

## Architecture

Client → Controller → Service → Repository → PostgreSQL
