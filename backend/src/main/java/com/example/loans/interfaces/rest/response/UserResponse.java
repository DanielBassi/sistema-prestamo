package com.example.loans.interfaces.rest.response;

import com.example.loans.domain.enumtype.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
