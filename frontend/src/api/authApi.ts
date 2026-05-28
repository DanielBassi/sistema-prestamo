import { axiosClient } from "./axiosClient";
import type { AuthResponse, LoginRequest } from "../features/auth/authTypes";

const BASE_URL = "/api/auth/login";

export async function login(payload: LoginRequest): Promise<AuthResponse> {
  const { data } = await axiosClient.post<AuthResponse>(BASE_URL, payload);
  return data;
}
