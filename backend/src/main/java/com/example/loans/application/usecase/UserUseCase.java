package com.example.loans.application.usecase;

import com.example.loans.domain.model.User;
import com.example.loans.interfaces.rest.response.UserResponse;

import java.util.List;

public interface UserUseCase {

    List<UserResponse> getAllUsers(User requester);

    UserResponse getUserById(Long id, User requester);
}
