import { forwardRef, type InputHTMLAttributes } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(function Input(
  { label, error, className, ...props },
  ref
) {
  return (
    <div className="space-y-1">
      <label className="text-sm font-medium text-slate-700">{label}</label>
      <input
        ref={ref}
        className={`w-full rounded-md border px-3 py-2 text-sm text-slate-900 outline-none transition focus:border-slate-500 focus:ring-2 focus:ring-slate-200 ${error ? "border-rose-400" : "border-slate-300"} ${className ?? ""}`}
        {...props}
      />
      {error ? <p className="text-xs text-rose-600">{error}</p> : null}
    </div>
  );
});
