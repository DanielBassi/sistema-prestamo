import { Link } from "react-router-dom";

export function UnauthorizedPage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-100 p-4">
      <div className="max-w-md rounded-xl bg-white p-8 text-center shadow-sm">
        <h1 className="mb-3 text-2xl font-bold text-slate-900">Acceso no autorizado</h1>
        <p className="mb-6 text-slate-600">No tienes permisos para acceder a esta ruta.</p>
        <Link className="text-sm font-semibold text-slate-900 underline" to="/login">
          Volver al login
        </Link>
      </div>
    </div>
  );
}

