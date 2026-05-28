import {
  createBrowserRouter,
  Navigate,
  Outlet,
  useNavigate,
  useLocation,
} from "react-router-dom";
import { useEffect } from "react";
import { LoginPage } from "../features/auth/LoginPage";
import { ProtectedRoute } from "../features/auth/ProtectedRoute";
import { RoleRoute } from "../features/auth/RoleRoute";
import { AppLayout } from "../components/layout/AppLayout";
import { UserLoansPage } from "../features/loans/UserLoansPage";
import { AdminLoansPage } from "../features/admin/AdminLoansPage";
import { UnauthorizedPage } from "../pages/UnauthorizedPage";
import { NotFoundPage } from "../pages/NotFoundPage";
import { registerUnauthorizedHandler } from "../api/axiosClient";
import { useAuthStore } from "../features/auth/authStore";

function AuthBootstrap() {
  const logout = useAuthStore((state) => state.logout);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    registerUnauthorizedHandler(() => {
      logout();
      navigate("/login", {
        replace: true,
        state: { reason: "session-expired", from: location.pathname },
      });
    });
  }, [logout, navigate, location.pathname]);

  return <Outlet />;
}

function RedirectByRole() {
  const role = useAuthStore((state) => state.role);
  return <Navigate to={role === "ADMIN" ? "/admin/loans" : "/loans"} replace />;
}

function LogoutOnMount(): null {
  const logout = useAuthStore((state) => state.logout);
  useEffect(() => {
    logout();
  }, [logout]);
  return null;
}

export const router = createBrowserRouter([
  {
    element: <AuthBootstrap />,
    children: [
      { path: "/login", element: <LoginPage /> },
      {
        element: <ProtectedRoute />,
        children: [
          {
            element: <AppLayout />,
            children: [
              {
                path: "/loans",
                element: <RoleRoute allowedRoles={["USER"]} />,
                children: [{ index: true, element: <UserLoansPage /> }],
              },
              {
                path: "/admin/loans",
                element: <RoleRoute allowedRoles={["ADMIN"]} />,
                children: [{ index: true, element: <AdminLoansPage /> }],
              },
            ],
          },
        ],
      },
      { path: "/", element: <RedirectByRole /> },
      { path: "/logout", element: <LogoutOnMount /> },
      { path: "/unauthorized", element: <UnauthorizedPage /> },
      { path: "*", element: <NotFoundPage /> },
    ],
  },
]);

