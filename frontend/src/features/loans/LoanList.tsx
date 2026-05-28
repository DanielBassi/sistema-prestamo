import type { Loan } from "./loanTypes";
import { Card } from "../../components/ui/Card";
import { Badge } from "../../components/ui/Badge";
import { formatCurrency } from "../../utils/formatCurrency";
import { formatDate } from "../../utils/formatDate";

interface LoanListProps {
  loans: Loan[];
}

export function LoanList({ loans }: LoanListProps) {
  if (loans.length === 0) {
    return <Card>No tienes prestamos registrados por ahora.</Card>;
  }

  return (
    <div className="space-y-3">
      {loans.map((loan) => (
        <Card key={loan.id} className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="space-y-1">
            <p className="text-sm font-semibold text-slate-800">Prestamo #{loan.id}</p>
            <p className="text-sm text-slate-600">Monto: {formatCurrency(loan.amount)}</p>
            <p className="text-sm text-slate-600">Plazo: {loan.termInMonths} meses</p>
            <p className="text-xs text-slate-500">Creado: {formatDate(loan.createdAt)}</p>
          </div>
          <Badge status={loan.status} />
        </Card>
      ))}
    </div>
  );
}

