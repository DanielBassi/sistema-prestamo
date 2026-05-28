package com.example.loans.interfaces.rest.controller;

import com.example.loans.application.usecase.LoanUseCase;
import com.example.loans.interfaces.rest.response.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/loans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLoanController {

    private final LoanUseCase loanService;

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAll() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<LoanResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LoanResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.rejectLoan(id));
    }
}
