package com.example.loans.interfaces.rest.response;

import com.example.loans.domain.enumtype.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        BigDecimal amount,
        Integer termInMonths,
        LoanStatus status,
        Long userId,
        String userEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime decisionAt
) {
}
