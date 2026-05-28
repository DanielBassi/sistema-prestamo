import type { UserRole } from "./authStore";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  email: string;
  role: UserRole;
}
