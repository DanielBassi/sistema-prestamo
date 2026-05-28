import { useEffect, useState } from "react";
import { getMyLoans } from "../../api/loansApi";
import { getApiErrorMessage } from "../../api/axiosClient";
import { useAuthStore } from "../auth/authStore";
import type { Loan } from "./loanTypes";
import { LoanRequestForm } from "./LoanRequestForm";
import { LoanList } from "./LoanList";
import { Loading } from "../../components/ui/Loading";
import { ErrorMessage } from "../../components/ui/ErrorMessage";
import { Button } from "../../components/ui/Button";

export function UserLoansPage() {
  const email = useAuthStore((state) => state.email);
  const logout = useAuthStore((state) => state.logout);
  const [loans, setLoans] = useState<Loan[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchLoans = async () => {
    try {
      setError(null);
      const data = await getMyLoans();
      setLoans(data);
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void fetchLoans();
  }, []);

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h1 className="text-3xl font-bold text-slate-900">Bienvenido, Usuario</h1>
        <div className="flex items-center gap-3">
          <span className="text-sm text-slate-500">{email}</span>
          <Button onClick={logout}>Cerrar sesion</Button>
        </div>
      </div>

      <LoanRequestForm onSuccess={fetchLoans} />

      <section className="space-y-4">
        <h2 className="text-xl font-semibold text-slate-900">Mis Prestamos</h2>
        {loading ? <Loading /> : null}
        {error ? <ErrorMessage message={error} /> : null}
        {!loading && !error ? <LoanList loans={loans} /> : null}
      </section>
    </section>
  );
}

