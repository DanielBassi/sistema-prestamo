package com.example.loans.infrastructure.persistence.repository;

import com.example.loans.domain.exception.ResourceNotFoundException;
import com.example.loans.domain.model.Loan;
import com.example.loans.domain.repository.LoanRepository;
import com.example.loans.infrastructure.persistence.entity.LoanEntity;
import com.example.loans.infrastructure.persistence.entity.UserEntity;
import com.example.loans.infrastructure.persistence.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {

    private final SpringDataLoanRepository repository;
    private final SpringDataUserRepository userRepository;
    private final LoanMapper mapper;

    @Override
    public Loan save(Loan loan) {
        UserEntity user = userRepository.findById(loan.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LoanEntity existing = loan.getId() == null ? null : repository.findById(loan.getId()).orElse(null);
        LoanEntity entity = mapper.toEntity(loan, existing, user);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        return repository.findByUser_Id(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
