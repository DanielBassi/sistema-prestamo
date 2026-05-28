package com.example.loans.infrastructure.persistence.mapper;

import com.example.loans.domain.model.Loan;
import com.example.loans.infrastructure.persistence.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanMapper {

    private final UserMapper userMapper;

    public Loan toDomain(LoanEntity entity) {
        return Loan.builder()
            .id(entity.getId())
            .amount(entity.getAmount())
            .termInMonths(entity.getTermInMonths())
            .status(entity.getStatus())
            .user(userMapper.toDomain(entity.getUser()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .decisionAt(entity.getDecisionAt())
            .build();
    }

    public LoanEntity toEntity(Loan domain, LoanEntity base, com.example.loans.infrastructure.persistence.entity.UserEntity userEntity) {
        LoanEntity entity = base == null ? new LoanEntity() : base;
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setTermInMonths(domain.getTermInMonths());
        entity.setStatus(domain.getStatus());
        entity.setUser(userEntity);
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDecisionAt(domain.getDecisionAt());
        return entity;
    }
}
