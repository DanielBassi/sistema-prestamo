package com.example.loans.application.usecase;

import com.example.loans.domain.model.User;
import com.example.loans.interfaces.rest.response.LoanResponse;

import java.math.BigDecimal;
import java.util.List;

public interface LoanUseCase {

    LoanResponse createLoan(BigDecimal amount, Integer termInMonths, User requester);

    List<LoanResponse> getMyLoans(User requester);

    LoanResponse getLoanById(Long loanId, User requester);

    List<LoanResponse> getAllLoans();

    LoanResponse approveLoan(Long loanId);

    LoanResponse rejectLoan(Long loanId);
}
