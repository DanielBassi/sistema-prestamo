import { axiosClient } from "./axiosClient";
import type { CreateLoanRequest, Loan } from "../features/loans/loanTypes";

const BASE_URL = "/api/loans";

export async function createLoan(payload: CreateLoanRequest): Promise<Loan> {
  const { data } = await axiosClient.post<Loan>(BASE_URL, payload);
  return data;
}

export async function getMyLoans(): Promise<Loan[]> {
  const { data } = await axiosClient.get<Loan[]>(`${BASE_URL}/my`);
  return data;
}

export async function getLoanById(id: number): Promise<Loan> {
  const { data } = await axiosClient.get<Loan>(`${BASE_URL}/${id}`);
  return data;
}
