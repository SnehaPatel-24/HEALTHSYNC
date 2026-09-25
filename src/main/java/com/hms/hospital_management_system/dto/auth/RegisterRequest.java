package com.hms.hospital_management_system.dto.auth;

import com.hms.hospital_management_system.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email Format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Role role;

}
