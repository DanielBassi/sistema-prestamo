package com.example.loans.interfaces.rest.controller;

import com.example.loans.application.usecase.CurrentUserProvider;
import com.example.loans.application.usecase.LoanUseCase;
import com.example.loans.interfaces.rest.request.CreateLoanRequest;
import com.example.loans.interfaces.rest.response.LoanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanUseCase loanService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        return ResponseEntity.ok(
                loanService.createLoan(request.amount(), request.termInMonths(), currentUserProvider.getCurrentUser())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<LoanResponse>> getMyLoans() {
        return ResponseEntity.ok(loanService.getMyLoans(currentUserProvider.getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id, currentUserProvider.getCurrentUser()));
    }
}
