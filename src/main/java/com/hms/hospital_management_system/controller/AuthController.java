package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.auth.LoginRequest;
import com.hms.hospital_management_system.dto.auth.LoginResponse;
import com.hms.hospital_management_system.dto.auth.RegisterRequest;
import com.hms.hospital_management_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication APIs",
        description = "APIs for user registration, authentication, and JWT token generation."
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a New User",
            description = "Creates a new user account. Returns 201 CREATED on success."
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        String result = authService.register(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED); // 201
    }

    @Operation(
            summary = "Authenticate User",
            description = "Authenticates a user and returns a JWT access token. Returns 200 OK on success."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK); // 200
    }

    @Operation(
            summary = "Reset Password",
            description = "Resets the password for a registered email. Returns 200 OK on success."
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody java.util.Map<String, String> request
    ) {
        String email       = request.get("email");
        String newPassword = request.get("newPassword");
        authService.resetPassword(email, newPassword);
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK); // 200
    }

}
