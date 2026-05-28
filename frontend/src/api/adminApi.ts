import { axiosClient } from "./axiosClient";
import type { Loan } from "../features/loans/loanTypes";

const BASE_URL = "/api/admin/loans";

export async function getAllLoans(): Promise<Loan[]> {
  const { data } = await axiosClient.get<Loan[]>(BASE_URL);
  return data;
}

export async function approveLoan(id: number): Promise<Loan> {
  const { data } = await axiosClient.patch<Loan>(`${BASE_URL}/${id}/approve`);
  return data;
}

export async function rejectLoan(id: number): Promise<Loan> {
  const { data } = await axiosClient.patch<Loan>(`${BASE_URL}/${id}/reject`);
  return data;
}
