import { useEffect, useState } from "react";
import { approveLoan, getAllLoans, rejectLoan } from "../../api/adminApi";
import { getApiErrorMessage } from "../../api/axiosClient";
import { useAuthStore } from "../auth/authStore";
import type { Loan } from "../loans/loanTypes";
import { Loading } from "../../components/ui/Loading";
import { ErrorMessage } from "../../components/ui/ErrorMessage";
import { Button } from "../../components/ui/Button";
import { Card } from "../../components/ui/Card";
import { AdminLoanList } from "./AdminLoanList";

export function AdminLoansPage() {
  const logout = useAuthStore((state) => state.logout);
  const email = useAuthStore((state) => state.email);
  const [loans, setLoans] = useState<Loan[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [processingId, setProcessingId] = useState<number | null>(null);

  const fetchAll = async () => {
    try {
      setError(null);
      const data = await getAllLoans();
      setLoans(data);
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void fetchAll();
  }, []);

  const handleApprove = async (loanId: number) => {
    try {
      setProcessingId(loanId);
      await approveLoan(loanId);
      setMessage(`Prestamo #${loanId} aprobado correctamente.`);
      await fetchAll();
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setProcessingId(null);
    }
  };

  const handleReject = async (loanId: number) => {
    try {
      setProcessingId(loanId);
      await rejectLoan(loanId);
      setMessage(`Prestamo #${loanId} rechazado correctamente.`);
      await fetchAll();
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setProcessingId(null);
    }
  };

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h1 className="text-3xl font-bold text-slate-900">Hola, Admin</h1>
        <div className="flex items-center gap-3">
          <span className="text-sm text-slate-500">{email}</span>
          <Button onClick={logout}>Cerrar sesion</Button>
        </div>
      </div>

      <Card>
        <h2 className="mb-4 text-2xl font-semibold text-slate-900">Gestionar Solicitudes de Prestamos</h2>
        {message ? <p className="mb-4 rounded-md bg-emerald-50 px-3 py-2 text-sm text-emerald-700">{message}</p> : null}
        {loading ? <Loading /> : null}
        {error ? <ErrorMessage message={error} /> : null}
        {!loading && !error ? (
          <AdminLoanList
            loans={loans}
            processingId={processingId}
            onApprove={handleApprove}
            onReject={handleReject}
          />
        ) : null}
      </Card>
    </section>
  );
}

