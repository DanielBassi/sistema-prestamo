export const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

export const STORAGE_KEYS = {
  token: "loan_app_token",
  email: "loan_app_email",
  role: "loan_app_role",
} as const;
