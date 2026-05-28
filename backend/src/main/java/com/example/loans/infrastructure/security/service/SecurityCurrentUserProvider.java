package com.example.loans.infrastructure.security.service;

import com.example.loans.application.usecase.CurrentUserProvider;
import com.example.loans.domain.exception.ResourceNotFoundException;
import com.example.loans.domain.model.User;
import com.example.loans.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityCurrentUserProvider implements CurrentUserProvider {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
