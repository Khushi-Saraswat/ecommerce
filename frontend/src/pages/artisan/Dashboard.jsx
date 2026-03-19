

const KYC_COLOR = { VERIFIED: "green", PENDING: "amber", REJECTED: "red", UNVERIFIED: "blue" };

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

const Card = ({ children, className = "" }) => (
  <div className={`bg-white dark:bg-gray-800 rounded-2xl border border-gray-100 dark:border-gray-700 shadow-sm ${className}`}>
    {children}
  </div>
);

const statGradient = {
  primary: "from-primary-500 to-primary-600",
  green:   "from-green-500 to-green-600",
  amber:   "from-amber-400 to-amber-500",
  rose:    "from-rose-500 to-rose-600",
};

const StatCard = ({ label, value, icon, sub, color = "primary" }) => (
  <div className="bg-white dark:bg-gray-800 rounded-2xl p-5 border border-gray-100 dark:border-gray-700 flex items-center gap-4 shadow-sm">
    <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${statGradient[color]} flex items-center justify-center text-white text-xl shadow-lg`}>
      {icon}
    </div>
    <div>
      <p className="text-2xl font-bold text-gray-900 dark:text-white">{value ?? "—"}</p>
      <p className="text-xs font-medium text-gray-500 dark:text-gray-400">{label}</p>
      {sub && <p className="text-[11px] text-gray-400 mt-0.5">{sub}</p>}
    </div>
  </div>
);

export default function Dashboard({ profile, products, catRequests, onNav }) {
  const total   = products?.totalElements ?? 0;
  const active  = products?.content?.filter((p) => p.isActive !== false).length ?? 0;
  const pending = catRequests?.filter((c) => c.status === "PENDING").length ?? 0;

  return (
    <div className="space-y-8">

      {/* Welcome */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white text-center">
          Welcome back{profile?.brandName ? `, ${profile.brandName}` : ""}! 👋
        </h1>
        <p className="text-sm text-gray-500 dark:text-gray-400 mt-1 text-center">Here's a snapshot of your artisan store.</p>
      </div>

      {/* Stat cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard label="Total Products"    value={total}                     icon="▦" color="primary" sub="in your store" />
        <StatCard label="Active Listings"   value={active}                    icon="◉" color="green"   sub="visible to buyers" />
        <StatCard label="Pending Cat. Req." value={pending}                   icon="◎" color="amber"   sub="awaiting admin" />
        <StatCard label="KYC Status"        value={profile?.kycStatus ?? "—"} icon="◈" color="rose"    sub={profile?.city ?? ""} />
      </div>

      {/* Profile snapshot */}
      {profile && (
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-sm font-semibold text-gray-900 dark:text-white">Store Profile</h2>
            <button onClick={() => onNav("profile")} className="text-xs text-primary-600 hover:underline">Edit →</button>
          </div>
          <div className="flex items-start gap-5">
            <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary-500 to-primary-600 flex items-center justify-center text-white text-2xl font-bold flex-shrink-0">
              {profile.brandName?.[0] ?? "A"}
            </div>
            <div className="flex-1 min-w-0 space-y-1.5">
              <div className="flex items-center gap-2 flex-wrap">
                <p className="text-lg font-bold text-gray-900 dark:text-white">{profile.brandName}</p>
                <Badge color={KYC_COLOR[profile.kycStatus] ?? "blue"}>{profile.kycStatus ?? "UNVERIFIED"}</Badge>
              </div>
              <p className="text-sm text-primary-600 font-medium">{profile.artisianType}</p>
              <p className="text-sm text-gray-500 dark:text-gray-400 line-clamp-2">{profile.bio}</p>
              <div className="flex items-center gap-4 flex-wrap text-xs text-gray-400">
                {profile.name     && <span>👤 {profile.name}</span>}
                {profile.username && <span>🔖 @{profile.username}</span>}
                <span>📍 {[profile.city, profile.state, profile.pincode].filter(Boolean).join(", ")}</span>
              </div>
            </div>
          </div>
        </Card>
      )}

      {/* Recent products */}
      {products?.content?.length > 0 && (
        <Card className="overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100 dark:border-gray-700 flex items-center justify-between">
            <h2 className="text-sm font-semibold text-gray-900 dark:text-white">Recent Products</h2>
            <button onClick={() => onNav("products")} className="text-xs text-primary-600 hover:underline">View all →</button>
          </div>
          <div className="divide-y divide-gray-50 dark:divide-gray-700/50">
            {products.content.slice(0, 5).map((p) => (
              <div key={p.id} className="px-6 py-3 flex items-center gap-4 hover:bg-gray-50 dark:hover:bg-gray-700/20 transition-colors">
                <div className="w-9 h-9 rounded-lg bg-primary-50 flex items-center justify-center text-primary-600 font-bold flex-shrink-0">
                  {p.name?.[0]}
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-900 dark:text-white truncate">{p.name}</p>
                  <p className="text-xs text-gray-400">
                    ₹{p.price}
                    {p.mrp && p.mrp > p.price && <span className="line-through ml-1 text-gray-300">₹{p.mrp}</span>}
                    <span className="ml-3 text-gray-300">· stock: {p.stock ?? "—"}</span>
                  </p>
                </div>
                <Badge color={p.isActive !== false ? "green" : "red"}>
                  {p.isActive !== false ? "Active" : "Inactive"}
                </Badge>
              </div>
            ))}
          </div>
        </Card>
      )}

    </div>
  );
}
