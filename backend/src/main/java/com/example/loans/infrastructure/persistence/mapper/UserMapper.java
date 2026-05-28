package com.example.loans.infrastructure.persistence.mapper;

import com.example.loans.domain.model.User;
import com.example.loans.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return User.builder()
            .id(entity.getId())
            .name(entity.getName())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .role(entity.getRole())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
