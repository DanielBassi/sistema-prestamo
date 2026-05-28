package com.example.loans.application.service;

import com.example.loans.application.usecase.LoanUseCase;
import com.example.loans.domain.enumtype.LoanStatus;
import com.example.loans.domain.enumtype.Role;
import com.example.loans.domain.exception.BusinessConflictException;
import com.example.loans.domain.exception.ResourceNotFoundException;
import com.example.loans.domain.exception.UnauthorizedAccessException;
import com.example.loans.domain.model.Loan;
import com.example.loans.domain.model.User;
import com.example.loans.domain.repository.LoanRepository;
import com.example.loans.interfaces.rest.response.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanUseCase {

    private final LoanRepository loanRepository;

    // Se invalida la caché de listados porque al crear un préstamo
    // cambian los resultados de "mis préstamos" y "todos los préstamos".
    @Override
    @Transactional
    @CacheEvict(cacheNames = {"loansByUser", "allLoans"}, allEntries = true)
    public LoanResponse createLoan(BigDecimal amount, Integer termInMonths, User requester) {
        
        if (amount == null || amount.signum() <= 0) 
            throw new IllegalArgumentException("El monto del préstamo debe ser mayor que cero.");

        if (termInMonths == null || termInMonths <= 0)
            throw new IllegalArgumentException("El plazo del préstamo debe ser mayor que cero.");
        
        Loan loan = Loan.builder()
            .amount(amount)
            .termInMonths(termInMonths)
            .status(LoanStatus.PENDING)
            .user(requester)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return toResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "loansByUser", key = "#requester.id")
    public List<LoanResponse> getMyLoans(User requester) {
        return loanRepository.findByUserId(requester.getId()).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "loanById", key = "#loanId")
    public LoanResponse getLoanById(Long loanId, User requester) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Prestamo no encontrado"));

        if (requester.getRole() != Role.ADMIN && !loan.getUser().getId().equals(requester.getId()))
            throw new UnauthorizedAccessException("No tienes permiso para acceder a este préstamo.");

        return toResponse(loan);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "allLoans")
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream().map(this::toResponse).toList();
    }

    // La aprobación es transaccional para garantizar que el cambio de estado
    // del préstamo se confirme de forma atómica.
    @Override
    @Transactional
    @CachePut(cacheNames = "loanById", key = "#loanId")
    @CacheEvict(cacheNames = {"loansByUser", "allLoans"}, allEntries = true)
    public LoanResponse approveLoan(Long loanId) {
        return decide(loanId, LoanStatus.APPROVED);
    }

    // Al rechazar un préstamo se actualiza la caché individual y se invalidan
    // los listados para evitar mostrar estados desactualizados.
    @Override
    @Transactional
    @CachePut(cacheNames = "loanById", key = "#loanId")
    @CacheEvict(cacheNames = {"loansByUser", "allLoans"}, allEntries = true)
    public LoanResponse rejectLoan(Long loanId) {
        return decide(loanId, LoanStatus.REJECTED);
    }

    private LoanResponse decide(Long loanId, LoanStatus targetStatus) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Prestamo no encontrado"));

        if (loan.getStatus() != LoanStatus.PENDING)
            throw new BusinessConflictException("Solo los préstamos pendientes pueden ser decididos");
        
        Loan updated = Loan.builder()
            .id(loan.getId())
            .amount(loan.getAmount())
            .termInMonths(loan.getTermInMonths())
            .status(targetStatus)
            .user(loan.getUser())
            .createdAt(loan.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .decisionAt(LocalDateTime.now())
            .build();
        return toResponse(loanRepository.save(updated));
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
            loan.getId(),
            loan.getAmount(),
            loan.getTermInMonths(),
            loan.getStatus(),
            loan.getUser().getId(),
            loan.getUser().getEmail(),
            loan.getCreatedAt(),
            loan.getUpdatedAt(),
            loan.getDecisionAt()
        );
    }
}
