import type { ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  loading?: boolean;
  fullWidth?: boolean;
  variant?: "primary" | "secondary" | "danger";
}

const variantStyles = {
  primary: "bg-slate-900 text-white hover:bg-slate-800",
  secondary: "bg-emerald-600 text-white hover:bg-emerald-500",
  danger: "bg-rose-600 text-white hover:bg-rose-500",
};

export function Button({
  children,
  loading,
  fullWidth,
  disabled,
  className,
  variant = "primary",
  ...props
}: ButtonProps) {
  return (
    <button
      className={`rounded-md px-4 py-2 text-sm font-semibold transition disabled:cursor-not-allowed disabled:opacity-60 ${variantStyles[variant]} ${fullWidth ? "w-full" : ""} ${className ?? ""}`}
      disabled={disabled || loading}
      {...props}
    >
      {loading ? "Procesando..." : children}
    </button>
  );
}

