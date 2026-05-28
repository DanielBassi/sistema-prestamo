package com.example.loans.interfaces.rest.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateLoanRequest(
        @NotNull @DecimalMin(value = "0.01", message = "El monto del préstamo debe ser mayor que cero.") BigDecimal amount,
        @NotNull @Positive(message = "El plazo del préstamo debe ser mayor que cero.") Integer termInMonths
) {
}
