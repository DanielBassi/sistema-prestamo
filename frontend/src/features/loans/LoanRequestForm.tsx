import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createLoan } from "../../api/loansApi";
import { getApiErrorMessage } from "../../api/axiosClient";
import { Card } from "../../components/ui/Card";
import { Input } from "../../components/ui/Input";
import { Button } from "../../components/ui/Button";
import { ErrorMessage } from "../../components/ui/ErrorMessage";

const requestSchema = z.object({
  amount: z.number().gt(0, "El monto debe ser mayor que 0"),
  termInMonths: z.number().gt(0, "El plazo debe ser mayor que 0"),
});

type LoanRequestFormValues = z.infer<typeof requestSchema>;

interface LoanRequestFormProps {
  onSuccess: () => Promise<void>;
}

export function LoanRequestForm({ onSuccess }: LoanRequestFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
    setError,
    clearErrors,
  } = useForm<LoanRequestFormValues>({
    resolver: zodResolver(requestSchema),
    defaultValues: {
      amount: 0,
      termInMonths: 0,
    },
  });

  const onSubmit = async (values: LoanRequestFormValues) => {
    try {
      clearErrors("root");
      await createLoan(values);
      reset({ amount: 0, termInMonths: 0 });
      await onSuccess();
    } catch (error) {
      setError("root", { message: getApiErrorMessage(error) });
    }
  };

  return (
    <Card>
      <h2 className="mb-4 text-xl font-semibold text-slate-900">Solicitar Prestamo</h2>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <Input
          label="Monto"
          type="number"
          step="0.01"
          min="1"
          error={errors.amount?.message}
          {...register("amount", { valueAsNumber: true })}
        />
        <Input
          label="Plazo en meses"
          type="number"
          min="1"
          error={errors.termInMonths?.message}
          {...register("termInMonths", { valueAsNumber: true })}
        />
        {errors.root?.message ? <ErrorMessage message={errors.root.message} /> : null}
        <Button loading={isSubmitting} type="submit">
          Enviar solicitud
        </Button>
      </form>
    </Card>
  );
}

