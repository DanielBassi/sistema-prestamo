package com.example.loans.infrastructure.config;

import com.example.loans.domain.enumtype.Role;
import com.example.loans.infrastructure.persistence.entity.UserEntity;
import com.example.loans.infrastructure.persistence.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SpringDataUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createIfNotExists("Usuario Demo", "usuario@test.com", "123", Role.USER);
        createIfNotExists("Administrador Demo", "admin@test.com", "123", Role.ADMIN);
    }

    private void createIfNotExists(String name, String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isPresent()) return;

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
