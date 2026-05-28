package com.example.loans.application.service;

import com.example.loans.domain.enumtype.LoanStatus;
import com.example.loans.domain.enumtype.Role;
import com.example.loans.domain.exception.BusinessConflictException;
import com.example.loans.domain.exception.UnauthorizedAccessException;
import com.example.loans.domain.model.Loan;
import com.example.loans.domain.model.User;
import com.example.loans.domain.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private User user;
    private User admin;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("u@test.com").name("u").role(Role.USER).password("x").createdAt(LocalDateTime.now()).build();
        admin = User.builder().id(2L).email("a@test.com").name("a").role(Role.ADMIN).password("x").createdAt(LocalDateTime.now()).build();
    }

    @Test
    void shouldCreateLoanSuccessfully() {
        Loan saved = Loan.builder().id(10L).amount(BigDecimal.valueOf(1000)).termInMonths(12).status(LoanStatus.PENDING)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(loanRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(saved);
        var result = loanService.createLoan(BigDecimal.valueOf(1000), 12, user);
        assertEquals(LoanStatus.PENDING, result.status());
        assertEquals(10L, result.id());
    }

    @Test
    void shouldRejectInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan(BigDecimal.ZERO, 12, user));
    }

    @Test
    void shouldApprovePendingLoan() {
        Loan pending = Loan.builder().id(11L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.PENDING)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        Loan approved = Loan.builder().id(11L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.APPROVED)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).decisionAt(LocalDateTime.now()).build();
        when(loanRepository.findById(11L)).thenReturn(Optional.of(pending));
        when(loanRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(approved);
        assertEquals(LoanStatus.APPROVED, loanService.approveLoan(11L).status());
    }

    @Test
    void shouldRejectPendingLoan() {
        Loan pending = Loan.builder().id(12L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.PENDING)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        Loan rejected = Loan.builder().id(12L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.REJECTED)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).decisionAt(LocalDateTime.now()).build();
        when(loanRepository.findById(12L)).thenReturn(Optional.of(pending));
        when(loanRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(rejected);
        assertEquals(LoanStatus.REJECTED, loanService.rejectLoan(12L).status());
    }

    @Test
    void shouldNotApproveAlreadyApprovedLoan() {
        Loan approved = Loan.builder().id(13L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.APPROVED)
                .user(user).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(loanRepository.findById(13L)).thenReturn(Optional.of(approved));
        assertThrows(BusinessConflictException.class, () -> loanService.approveLoan(13L));
    }

    @Test
    void shouldPreventUserFromAccessingOtherUserLoan() {
        Loan otherLoan = Loan.builder().id(14L).amount(BigDecimal.ONE).termInMonths(6).status(LoanStatus.PENDING)
                .user(admin).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(loanRepository.findById(14L)).thenReturn(Optional.of(otherLoan));
        assertThrows(UnauthorizedAccessException.class, () -> loanService.getLoanById(14L, user));
    }
}
