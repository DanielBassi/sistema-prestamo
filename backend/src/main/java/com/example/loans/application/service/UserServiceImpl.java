package com.example.loans.application.service;

import com.example.loans.application.usecase.UserUseCase;
import com.example.loans.domain.enumtype.Role;
import com.example.loans.domain.exception.ResourceNotFoundException;
import com.example.loans.domain.exception.UnauthorizedAccessException;
import com.example.loans.domain.model.User;
import com.example.loans.domain.repository.UserRepository;
import com.example.loans.interfaces.rest.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(User requester) {
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Solo los administradores pueden acceder a la lista de usuarios.");
        }

        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id, User requester) {
        if (requester.getRole() != Role.ADMIN && !requester.getId().equals(id)) {
            throw new UnauthorizedAccessException("No tienes permiso para acceder a este usuario.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        );
    }
}
