package com.example.loans.domain.model;

import com.example.loans.domain.enumtype.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class User {
    Long id;
    String name;
    String email;
    String password;
    Role role;
    LocalDateTime createdAt;
}
