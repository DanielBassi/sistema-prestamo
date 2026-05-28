package com.example.loans.application.usecase;

import com.example.loans.domain.model.User;

public interface CurrentUserProvider {
    User getCurrentUser();
}
