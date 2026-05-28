import { create } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";
import { STORAGE_KEYS } from "../../utils/constants";

export type UserRole = "USER" | "ADMIN";

interface AuthState {
  token: string | null;
  email: string | null;
  role: UserRole | null;
  isAuthenticated: boolean;
  login: (params: { token: string; email: string; role: UserRole }) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      email: null,
      role: null,
      isAuthenticated: false,
      login: ({ token, email, role }) =>
        set({
          token,
          email,
          role,
          isAuthenticated: true,
        }),
      logout: () =>
        set({
          token: null,
          email: null,
          role: null,
          isAuthenticated: false,
        }),
    }),
    {
      name: "loan-auth-storage",
      storage: createJSONStorage(() => localStorage),
      partialize: (state) => ({
        token: state.token,
        email: state.email,
        role: state.role,
      }),
      onRehydrateStorage: () => (state) => {
        if (state) {
          state.isAuthenticated = Boolean(state.token);
        }
      },
    }
  )
);

export function getStoredToken(): string | null {
  return useAuthStore.getState().token ?? localStorage.getItem(STORAGE_KEYS.token);
}
