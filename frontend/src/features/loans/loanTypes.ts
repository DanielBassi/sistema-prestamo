export type LoanStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface Loan {
  id: number;
  amount: number;
  termInMonths: number;
  status: LoanStatus;
  userEmail?: string;
  createdAt?: string;
  updatedAt?: string;
  decisionAt?: string;
}

export interface CreateLoanRequest {
  amount: number;
  termInMonths: number;
}
