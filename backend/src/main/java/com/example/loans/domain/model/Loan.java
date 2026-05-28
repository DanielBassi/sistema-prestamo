package com.example.loans.domain.model;

import com.example.loans.domain.enumtype.LoanStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class Loan {
    Long id;
    BigDecimal amount;
    Integer termInMonths;
    LoanStatus status;
    User user;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime decisionAt;
}
