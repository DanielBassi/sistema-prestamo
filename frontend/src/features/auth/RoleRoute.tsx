import { Navigate, Outlet } from "react-router-dom";
import { useAuthStore, type UserRole } from "./authStore";

interface RoleRouteProps {
  allowedRoles: UserRole[];
}

export function RoleRoute({ allowedRoles }: RoleRouteProps) {
  const role = useAuthStore((state) => state.role);

  if (!role || !allowedRoles.includes(role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}

