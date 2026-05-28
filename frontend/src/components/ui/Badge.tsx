import type { LoanStatus } from "../../features/loans/loanTypes";

interface BadgeProps {
  status: LoanStatus;
}

const statusStyles: Record<LoanStatus, string> = {
  PENDING: "bg-amber-100 text-amber-700",
  APPROVED: "bg-emerald-100 text-emerald-700",
  REJECTED: "bg-rose-100 text-rose-700",
};

export function Badge({ status }: BadgeProps) {
  return (
    <span className={`rounded-full px-3 py-1 text-xs font-semibold ${statusStyles[status]}`}>
      {status}
    </span>
  );
}

