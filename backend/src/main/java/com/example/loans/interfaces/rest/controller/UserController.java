package com.example.loans.interfaces.rest.controller;

import com.example.loans.application.usecase.CurrentUserProvider;
import com.example.loans.application.usecase.UserUseCase;
import com.example.loans.interfaces.rest.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers(currentUserProvider.getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id, currentUserProvider.getCurrentUser()));
    }
}
