// ─── CategoryRequests.jsx ─────────────────────────────────────
// Only depends on: react, ./api
// Usage: <CategoryRequests requests={} loading={} onRefresh={fn} showToast={fn} />

import { useState } from "react";
import { artisanAPI } from "../../services/api";

const STATUS_COLOR = { PENDING: "amber", APPROVED: "green", REJECTED: "red" };
const STATUS_ICON  = { PENDING: "⏳",    APPROVED: "✓",     REJECTED: "✗"  };

const badgeMap = {
  blue:  "bg-blue-50 text-blue-700 border-blue-200",
  green: "bg-green-50 text-green-700 border-green-200",
  amber: "bg-amber-50 text-amber-700 border-amber-200",
  red:   "bg-red-50 text-red-700 border-red-200",
};

const Badge = ({ children, color = "blue" }) => (
  <span className={`inline-flex px-2 py-0.5 text-[11px] font-semibold rounded-md border ${badgeMap[color]}`}>
    {children}
  </span>
);

const Spinner = () => (
  <div className="flex items-center justify-center py-16">
    <div className="w-8 h-8 border-2 border-primary-500 border-t-transparent rounded-full animate-spin" />
  </div>
);

const inputCls = `w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl
  bg-gray-50 dark:bg-gray-800/60 text-gray-900 dark:text-white placeholder-gray-400
  focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500 text-sm`;

export default function CategoryRequests({ requests, loading, onRefresh, showToast }) {
  const [catName,    setCatName]    = useState("");
  const [submitting, setSubmitting] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    if (!catName.trim()) return;
    setSubmitting(true);
    try {
      const response = await artisanAPI.createCategoryRequest(
           catName
      );
      console.log(response.data,"category");
      showToast("Category request submitted!", "success");
      setCatName("");
      onRefresh();
    } catch (err) {
      showToast(err.message, "error");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="space-y-7 max-w-2xl mx-auto w-100">

      {/* Header */}
      <div className="text-center">
        <h2 className="text-xl font-bold text-gray-900 dark:text-white ">Category Requests</h2>
        <p className="text-sm text-gray-500 mt-1">Request a new product category — admin reviews and approves.</p>
      </div>

      {/* New request form */}
      <div className="bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 p-6 mx-auto">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-4 ">New Request</h3>
        <form onSubmit={submit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-gray-500 uppercase tracking-wider mb-1.5">
              Category Name <span className="text-red-500">*</span>
            </label>
            <input
              className={inputCls}
              value={catName}
              onChange={(e) => setCatName(e.target.value)}
              required
              placeholder="e.g. Terracotta Art, Dhokra Craft, Warli Painting..."
            />
          </div>
          <button type="submit" disabled={submitting}
            className="w-full btn-primary py-2.5 text-sm flex items-center justify-center gap-2 disabled:opacity-60">
            {submitting && <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />}
            Submit Request
          </button>
        </form>
      </div>

      {/* Requests list */}
      <div className="bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 dark:border-gray-700 flex items-center justify-between">
          <h3 className="text-sm font-semibold text-gray-900 dark:text-white">My Requests</h3>
          <button onClick={onRefresh} className="text-xs text-primary-600 hover:underline">↻ Refresh</button>
        </div>

        {loading ? (
          <Spinner />
        ) : requests.length === 0 ? (
          <div className="px-6 py-12 text-center text-sm text-gray-400">No requests yet.</div>
        ) : (
          <div className="divide-y divide-gray-50 dark:divide-gray-700/50">
            {requests.map((r) => (
              <div key={r.id} className="px-6 py-4 flex items-start justify-between gap-4">
                <div className="min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <span className="text-sm">{STATUS_ICON[r.status] ?? "◎"}</span>
                    <p className="text-sm font-medium text-gray-900 dark:text-white">{r.name}</p>
                  </div>
                  {r.note && <p className="text-xs text-gray-400 mt-0.5 ml-5 italic">{r.note}</p>}
                  <div className="flex items-center gap-3 mt-1 ml-5 text-[11px] text-gray-400 flex-wrap">
                    {r.createdAt && <span>Created: {new Date(r.createdAt).toLocaleDateString()}</span>}
                    {r.updatedAt && r.updatedAt !== r.createdAt && <span>· Updated: {new Date(r.updatedAt).toLocaleDateString()}</span>}
                  </div>
                </div>
                <Badge color={STATUS_COLOR[r.status] ?? "blue"}>{r.status ?? "PENDING"}</Badge>
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}
