import type { Loan } from "../loans/loanTypes";
import { Card } from "../../components/ui/Card";
import { Badge } from "../../components/ui/Badge";
import { Button } from "../../components/ui/Button";
import { formatCurrency } from "../../utils/formatCurrency";
import { formatDate } from "../../utils/formatDate";

interface AdminLoanListProps {
  loans: Loan[];
  processingId: number | null;
  onApprove: (loanId: number) => Promise<void>;
  onReject: (loanId: number) => Promise<void>;
}

export function AdminLoanList({
  loans,
  processingId,
  onApprove,
  onReject,
}: AdminLoanListProps) {
  if (loans.length === 0) {
    return <Card>No hay solicitudes de prestamo por gestionar.</Card>;
  }

  return (
    <div className="space-y-3">
      {loans.map((loan) => {
        const isPending = loan.status === "PENDING";
        const isProcessing = processingId === loan.id;

        return (
          <Card key={loan.id} className="space-y-3">
            <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
              <div className="space-y-1 text-sm text-slate-700">
                <p className="font-semibold text-slate-900">Solicitud #{loan.id}</p>
                <p>Usuario: {loan.userEmail ?? "-"}</p>
                <p>Monto: {formatCurrency(loan.amount)}</p>
                <p>Plazo: {loan.termInMonths} meses</p>
                <p>Creado: {formatDate(loan.createdAt)}</p>
              </div>
              <Badge status={loan.status} />
            </div>
            {isPending ? (
              <div className="flex gap-2">
                <Button
                  variant="secondary"
                  loading={isProcessing}
                  disabled={isProcessing}
                  onClick={() => void onApprove(loan.id)}
                >
                  Aprobar
                </Button>
                <Button
                  variant="danger"
                  loading={isProcessing}
                  disabled={isProcessing}
                  onClick={() => void onReject(loan.id)}
                >
                  Rechazar
                </Button>
              </div>
            ) : null}
          </Card>
        );
      })}
    </div>
  );
}

