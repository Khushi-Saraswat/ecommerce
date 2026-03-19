// ─── ProductsList.jsx ─────────────────────────────────────────
// Only depends on: react
// Usage: <ProductsList products={} loading={} page={} onPageChange={} onDelete={} onEdit={} onAdd={} />

const badgeMap = {
  green: "bg-green-50 text-green-700 border-green-200",
  red:   "bg-red-50 text-red-700 border-red-200",
};

const Badge = ({ children, color = "green" }) => (
  <span className={`inline-flex px-2 py-0.5 text-[11px] font-semibold rounded-md border ${badgeMap[color]}`}>
    {children}
  </span>
);

const Spinner = () => (
  <div className="flex items-center justify-center py-16">
    <div className="w-8 h-8 border-2 border-primary-500 border-t-transparent rounded-full animate-spin" />
  </div>
);

export default function ProductsList({ products, loading, onDelete, onEdit, onPageChange, page, onAdd }) {
  if (loading) return <Spinner />;

  const items      = products?.content ?? [];
  const totalPages = products?.totalPages ?? 1;

  return (
    //product add
    <div className="space-y-6 mx-auto w-100">

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-xl font-bold text-gray-900 dark:text-white">My Products</h2>
          <p className="text-sm text-gray-500 mt-0.5">{products?.totalElements ?? 0} products</p>
        </div>
        <button onClick={onAdd} className="btn-primary px-4 py-2 text-sm flex items-center gap-1.5 text-center">
          <span>+</span> Add Product
        </button>
      </div>

      {/* Empty state */}
      {items.length === 0 ? (
        <div className="bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 p-16 text-center">
          <p className="text-4xl mb-3">📦</p>
          <p className="text-gray-500 font-medium">No products yet</p>
          <button onClick={onAdd} className="mt-4 btn-primary px-5 py-2 text-sm">Add First Product</button>
        </div>
      ) : (
        <div className="bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 overflow-hidden shadow-sm">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100 dark:border-gray-700">
                {["Product", "Price / MRP", "Stock", "Status", "Actions"].map((h) => (
                  <th key={h} className="px-5 py-3.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50 dark:divide-gray-700/50">
              {items.map((p) => (
                <tr key={p.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/20 transition-colors group">

                  {/* Name */}
                  <td className="px-5 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-xl bg-primary-50 flex items-center justify-center text-primary-600 font-bold flex-shrink-0">
                        {p.name?.[0]}
                      </div>
                      <div>
                        <p className="font-medium text-gray-900 dark:text-white">{p.name}</p>
                        <p className="text-xs text-gray-400 max-w-[180px] truncate">{p.description}</p>
                      </div>
                    </div>
                  </td>

                  {/* Price */}
                  <td className="px-5 py-4">
                    <p className="font-semibold text-gray-900 dark:text-white">₹{p.price}</p>
                    {p.mrp && p.mrp > p.price && <p className="text-xs text-gray-400 line-through">₹{p.mrp}</p>}
                  </td>

                  {/* Stock */}
                  <td className="px-5 py-4 text-gray-600 dark:text-gray-300">{p.stock ?? "—"}</td>

                  {/* Status */}
                  <td className="px-5 py-4">
                    <Badge color={p.isActive !== false ? "green" : "red"}>
                      {p.isActive !== false ? "Active" : "Inactive"}
                    </Badge>
                  </td>

                  {/* Actions */}
                  <td className="px-5 py-4">
                    <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <button onClick={() => onEdit(p)}
                        className="px-3 py-1.5 text-xs font-medium text-primary-600 bg-primary-50 rounded-lg hover:bg-primary-100 transition-colors">
                        Edit
                      </button>
                      <button onClick={() => onDelete(p.id)}
                        className="px-3 py-1.5 text-xs font-medium text-red-600 bg-red-50 rounded-lg hover:bg-red-100 transition-colors">
                        Delete
                      </button>
                    </div>
                  </td>

                </tr>
              ))}
            </tbody>
          </table>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="px-5 py-3.5 border-t border-gray-100 dark:border-gray-700 flex items-center justify-between">
              <p className="text-xs text-gray-500">Page {page + 1} of {totalPages}</p>
              <div className="flex gap-2">
                <button onClick={() => onPageChange(page - 1)} disabled={page === 0}
                  className="px-3 py-1.5 text-xs font-medium border border-gray-200 dark:border-gray-700 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors text-gray-600">
                  ← Prev
                </button>
                <button onClick={() => onPageChange(page + 1)} disabled={page >= totalPages - 1}
                  className="px-3 py-1.5 text-xs font-medium border border-gray-200 dark:border-gray-700 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors text-gray-600">
                  Next →
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
