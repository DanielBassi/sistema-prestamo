import axios, { AxiosError } from "axios";
import { API_URL } from "../utils/constants";
import { useAuthStore } from "../features/auth/authStore";

interface ApiError {
  message?: string;
}

let onUnauthorized: (() => void) | null = null;

export function registerUnauthorizedHandler(handler: () => void): void {
  onUnauthorized = handler;
}

export function getApiErrorMessage(error: unknown): string {
  const axiosError = error as AxiosError<ApiError>;
  const status = axiosError.response?.status;
  const backendMessage = axiosError.response?.data?.message;

  if (backendMessage) return backendMessage;

  switch (status) {
    case 400:
      return "Solicitud invalida. Verifica los datos enviados.";
    case 401:
      return "Tu sesion expiro o el token no es valido.";
    case 403:
      return "No tienes permisos para realizar esta accion.";
    case 404:
      return "No se encontro el recurso solicitado.";
    case 409:
      return "La operacion no pudo completarse por conflicto de estado.";
    default:
      return "Ocurrio un error inesperado. Intentalo nuevamente.";
  }
}

export const axiosClient = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosClient.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401 && onUnauthorized) {
      onUnauthorized();
    }
    return Promise.reject(error);
  }
);
