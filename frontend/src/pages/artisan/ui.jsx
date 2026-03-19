import { useState } from "react";

// ─── Badge ────────────────────────────────────────────────────────────────────
const badgeMap = {
  blue:  "bg-blue-50  text-blue-700  border-blue-200  dark:bg-blue-900/20  dark:text-blue-300  dark:border-blue-800",
  green: "bg-green-50 text-green-700 border-green-200 dark:bg-green-900/20 dark:text-green-300 dark:border-green-800",
  amber: "bg-amber-50 text-amber-700 border-amber-200 dark:bg-amber-900/20 dark:text-amber-300 dark:border-amber-800",
  red:   "bg-red-50   text-red-700   border-red-200   dark:bg-red-900/20   dark:text-red-300   dark:border-red-800",
};

export const Badge = ({ children, color = "blue" }) => (
  <span className={`inline-flex px-2 py-0.5 text-[11px] font-semibold rounded-md border ${badgeMap[color]}`}>
    {children}
  </span>
);

// ─── Spinner ──────────────────────────────────────────────────────────────────
export const Spinner = () => (
  <div className="flex items-center justify-center py-16">
    <div className="w-8 h-8 border-2 border-primary-500 border-t-transparent rounded-full animate-spin" />
  </div>
);

// ─── Toast ────────────────────────────────────────────────────────────────────
export const Toast = ({ msg, type }) => {
  if (!msg) return null;
  const cls =
    type === "success"
      ? "bg-green-50 dark:bg-green-900/30 text-green-800 dark:text-green-300 border-green-200 dark:border-green-800"
      : "bg-red-50 dark:bg-red-900/30 text-red-800 dark:text-red-300 border-red-200 dark:border-red-800";
  return (
    <div className={`fixed bottom-5 right-5 z-[9999] px-5 py-3 rounded-xl text-sm font-medium shadow-xl border ${cls}`}>
      {type === "success" ? "✓ " : "✗ "}{msg}
    </div>
  );
};

// ─── useToast hook ────────────────────────────────────────────────────────────
export function useToast() {
  const [toast, setToast] = useState({ msg: "", type: "success" });
  const show = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast({ msg: "", type }), 3500);
  };
  return { toast, show };
}

// ─── StatCard ─────────────────────────────────────────────────────────────────
const gradientMap = {
  primary: "from-primary-500 to-primary-600 shadow-primary-500/20",
  amber:   "from-amber-400  to-amber-500   shadow-amber-400/20",
  green:   "from-green-500  to-green-600   shadow-green-500/20",
  rose:    "from-rose-500   to-rose-600    shadow-rose-500/20",
};

export const StatCard = ({ label, value, icon, sub, color = "primary" }) => (
  <div className="bg-white dark:bg-gray-800 rounded-2xl p-5 border border-gray-100 dark:border-gray-700 flex items-center gap-4 shadow-sm hover:shadow-md transition-shadow">
    <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${gradientMap[color]} flex items-center justify-center text-white text-xl shadow-lg`}>
      {icon}
    </div>
    <div>
      <p className="text-2xl font-bold text-gray-900 dark:text-white">{value ?? "—"}</p>
      <p className="text-xs font-medium text-gray-500 dark:text-gray-400">{label}</p>
      {sub && <p className="text-[11px] text-gray-400 dark:text-gray-500 mt-0.5">{sub}</p>}
    </div>
  </div>
);

// ─── Field ────────────────────────────────────────────────────────────────────
export const Field = ({ label, required, hint, children }) => (
  <div>
    <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
      {label} {required && <span className="text-red-500">*</span>}
    </label>
    {children}
    {hint && <p className="text-[11px] text-gray-400 mt-1">{hint}</p>}
  </div>
);

// ─── Base input class ─────────────────────────────────────────────────────────
export const baseCls = `w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl
  bg-gray-50 dark:bg-gray-800/60 text-gray-900 dark:text-white placeholder-gray-400
  focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500
  transition-all text-sm`;

export const Inp = (props) => <input className={baseCls} {...props} />;
export const Sel = ({ children, ...p }) => (
  <select className={baseCls + " cursor-pointer"} {...p}>{children}</select>
);
export const Tex = (p) => <textarea className={baseCls + " resize-none"} {...p} />;

// ─── SectionCard ─────────────────────────────────────────────────────────────
export const SectionCard = ({ children, className = "" }) => (
  <div className={`bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 shadow-sm ${className}`}>
    {children}
  </div>
);

// ─── Divider ─────────────────────────────────────────────────────────────────
export const Divider = ({ label }) => (
  <div className="pt-2 border-t border-gray-100 dark:border-gray-700">
    {label && (
      <p className="text-xs font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest mt-3">
        {label}
      </p>
    )}
  </div>
);
