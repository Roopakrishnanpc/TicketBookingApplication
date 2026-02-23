Identity Service

Overview

The Identity Service is responsible for authentication and user management in the Movie Ticket Platform.
It provides secure login functionality and issues JWT tokens for authenticated users.

This service acts as the central authentication authority for all other microservices.

Responsibilities
	•	User registration and login
	•	Password encryption using BCrypt
	•	JWT token generation
	•	Role-based authentication support
	•	Stateless security configuration
	•	Integration with API Gateway
	•	OAuth-style token endpoint


Technology Stack
	•	Spring Boot
	•	Spring Security
	•	JWT (jjwt)
	•	MySQL
	•	JPA / Hibernate
	•	BCrypt Password Encoder



Authentication Flow
	1.	User submits credentials to /oauth/token
	2.	Identity Service validates credentials
	3.	Password is verified using BCrypt
	4.	JWT token is generated with:
	•	Username
	•	Roles
	•	Expiry time
	5.	Token is returned to client
	6.	Client uses token in Authorization header:

Authorization: Bearer <token>


	7.	Other services validate the JWT token


Security Features
	•	Stateless session management
	•	CSRF disabled (token-based authentication)
	•	Password hashing using BCrypt
	•	Role-based access control
	•	JWT signature verification
	•	Expiration-based token validation


API Endpoints

Generate Token
POST /oauth/register
{
  "username": "john",
  "password": "1234",
  "role": "CUSTOMER"
}

POST /oauth/token

Request Body:

{
  "grantType": "password",
  "email": "roopasr@mail.com",
  "password": "roopa123"
}
for refresh token
{
  "grantType": "refresh_token",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXJ0bmVyMSIsInJvbGUiOiJQQVJUTkVSIiwiaWF0IjoxNzcxNzQzMTA4LCJleHAiOjE3NzIzNDc5MDh9.M6mb5qy9Dl6Eklmv2NzI7RydeLvt7P4msicTq6HWwvM"
}
Response:

{
  "acccesstoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  "refreshtoken": "..."
}


⸻

JWT Token Structure
	•	Subject → Username
	•	Claims → Roles
	•	Expiration → Configurable
	•	Algorithm → HMAC SHA256

⸻

Database Schema

Users Table
	•	id (Primary Key)
	•	username
	•	password (BCrypt encoded)
	•	role


Configuration

Key properties in application.yml:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/identity_db
    username: root
    password: password

jwt:
  secret: your-secret-key
  expiration: 3600000



Deployment

Docker

docker build -t identity-service .
docker run -p 8081:8080 identity-service



Kubernetes
	•	Deployed as Deployment
	•	Exposed internally via ClusterIP
	•	Consumed by API Gateway


Future Enhancements
	•	OAuth2 Authorization Server integration
	•	Multi-factor authentication
	•	External identity provider integration (Google, Facebook)
	•	Key rotation for JWT signing
	•	Centralized secret management (AWS Secrets Manager / Azure Key Vault)

Architectural Role

Identity Service is a stateless authentication microservice that:
	•	Issues secure tokens
	•	Centralizes user authentication
	•	Enables role-based access control across the platform
	•	Integrates with API Gateway for secure routing

	•	Full root project README explaining entire architecture
