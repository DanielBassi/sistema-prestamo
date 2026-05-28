package com.example.loans.application.usecase;

import com.example.loans.interfaces.rest.response.AuthResponse;

public interface AuthUseCase {

    AuthResponse login(String email, String password);
}
