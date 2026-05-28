import { useAuthStore } from "../../features/auth/authStore";
import { Button } from "../ui/Button";

export function Navbar() {
  const email = useAuthStore((state) => state.email);
  const role = useAuthStore((state) => state.role);

  return (
    <header className="border-b border-slate-200 bg-white">
      <div className="mx-auto flex w-full max-w-6xl items-center justify-between px-4 py-4">
        <div>
          <p className="text-xs uppercase tracking-wide text-slate-500">Sistema de Prestamos</p>
          <p className="text-sm font-semibold text-slate-800">{email ?? "Sin sesion"}</p>
        </div>
        <Button type="button" variant={role === "ADMIN" ? "danger" : "primary"}>
          {role}
        </Button>
      </div>
    </header>
  );
}

