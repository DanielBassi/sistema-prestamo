package com.example.loans.interfaces.rest.response;

import com.example.loans.domain.enumtype.Role;

public record AuthResponse(
        String token,
        String type,
        String email,
        Role role
) {
}
