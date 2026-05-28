package com.example.loans.infrastructure.persistence.repository;

import com.example.loans.infrastructure.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataLoanRepository extends JpaRepository<LoanEntity, Long> {
    List<LoanEntity> findByUser_Id(Long userId);
}
