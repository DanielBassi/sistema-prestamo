package com.example.loans.domain.repository;

import com.example.loans.domain.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findByUserId(Long userId);
    List<Loan> findAll();
}
