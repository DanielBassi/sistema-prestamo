import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";
import { login } from "../../api/authApi";
import { getApiErrorMessage } from "../../api/axiosClient";
import { useAuthStore } from "./authStore";
import { Card } from "../../components/ui/Card";
import { Input } from "../../components/ui/Input";
import { Button } from "../../components/ui/Button";
import { ErrorMessage } from "../../components/ui/ErrorMessage";

const loginSchema = z.object({
  email: z.string().min(1, "El email es requerido").email("Email invalido"),
  password: z.string().min(1, "La contrasena es requerida"),
});

type LoginFormValues = z.infer<typeof loginSchema>;

export function LoginPage() {
  const navigate = useNavigate();
  const saveSession = useAuthStore((state) => state.login);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setError,
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = async (values: LoginFormValues) => {
    try {
      const response = await login(values);
      saveSession({
        token: response.token,
        email: response.email,
        role: response.role,
      });

      navigate(response.role === "ADMIN" ? "/admin/loans" : "/loans", {
        replace: true,
      });
    } catch (error) {
      setError("root", { message: getApiErrorMessage(error) });
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-100 px-4">
      <Card className="w-full max-w-md">
        <h1 className="mb-6 text-center text-2xl font-bold text-slate-900">Iniciar Sesion</h1>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input
            label="Correo Electronico"
            type="email"
            placeholder="usuario@test.com"
            error={errors.email?.message}
            {...register("email")}
          />
          <Input
            label="Contrasena"
            type="password"
            placeholder="********"
            error={errors.password?.message}
            {...register("password")}
          />
          {errors.root?.message ? <ErrorMessage message={errors.root.message} /> : null}
          <Button type="submit" fullWidth loading={isSubmitting}>
            Ingresar
          </Button>
        </form>
      </Card>
    </div>
  );
}

