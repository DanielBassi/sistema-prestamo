package com.example.loans.application.service;

import com.example.loans.application.usecase.AuthUseCase;
import com.example.loans.domain.exception.ResourceNotFoundException;
import com.example.loans.domain.model.User;
import com.example.loans.domain.repository.UserRepository;
import com.example.loans.infrastructure.security.jwt.JwtService;
import com.example.loans.interfaces.rest.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException ex) {
            throw ex;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, "Bearer", user.getEmail(), user.getRole());
    }
}
