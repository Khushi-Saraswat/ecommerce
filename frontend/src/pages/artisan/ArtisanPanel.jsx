// ArtisanPanel.jsx — copy to src/pages/artisan/ArtisanPanel.jsx
import { useEffect, useState } from "react";
import CategoryRequests from "./CategoryRequests";
import Dashboard from "./Dashboard";
import ProductForm from "./ProductForm";
import ProductsSection from "./ProductsSection";
import ProfileSection from "./ProfileSection";
import { api } from "./api";
import { KYC_COLOR } from "./constants";
import { Badge } from "./ui";

// ─── Nav config ───────────────────────────────────────────────
const NAV = [
  { id: "dashboard",  label: "Dashboard",        icon: "⬡" },
  { id: "profile",    label: "My Profile",        icon: "◈" },
  { id: "products",   label: "My Products",       icon: "▦" },
  { id: "addProduct", label: "Add Product",       icon: "✦" },
  { id: "categories", label: "Category Requests", icon: "◎" },
];

// ─── Toast ────────────────────────────────────────────────────
const Toast = ({ msg, type }) => {
  if (!msg) return null;
  const cls = type === "success"
    ? "bg-green-50 dark:bg-green-900/30 text-green-800 dark:text-green-300 border-green-200 dark:border-green-800"
    : "bg-red-50 dark:bg-red-900/30 text-red-800 dark:text-red-300 border-red-200 dark:border-red-800";
  return (
    <div className={`fixed bottom-5 right-5 z-[9999] px-5 py-3 rounded-xl text-sm font-medium shadow-xl border ${cls}`}>
      {type === "success" ? "✓ " : "✗ "}{msg}
    </div>
  );
};

function useToast() {
  const [toast, setToast] = useState({ msg: "", type: "success" });
  const show = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast({ msg: "", type }), 3500);
  };
  return { toast, show };
}

// ═════════════════════════════════════════════════════════════
export default function ArtisanPanel() {
  const [active,         setActive]   = useState("dashboard");
  const [sidebarOpen,    setSidebar]  = useState(false);
  const [profile,        setProfile]  = useState(null);
  const [products,       setProducts] = useState(null);
  const [catRequests,    setCatReqs]  = useState([]);
  const [prodLoading,    setProdLoad] = useState(false);
  const [catLoading,     setCatLoad]  = useState(false);
  const [profileLoading, setProf]     = useState(false);
  const [editProduct,    setEditProd] = useState(null);
  const [page,           setPage]     = useState(0);
  const { toast, show: showToast }    = useToast();

  // ── Data loaders ─────────────────────────────────────────────
  const loadProfile = async () => {
    setProf(true);
    try { setProfile(await api("GET", "/getArtisan")); }
    catch { /* profile may not exist yet */ }
    finally { setProf(false); }
  };

  const loadProducts = async (p = 0) => {
    setProdLoad(true);
    try { setProducts(await api("GET", `/artisanId/product?page=${p}&size=12&keyword=`)); }
    catch (err) { showToast(err.message, "error"); }
    finally { setProdLoad(false); }
  };

  const loadCatRequests = async () => {
    setCatLoad(true);
    try { setCatReqs(await api("GET", "/my")); }
    catch (err) { showToast(err.message, "error"); }
    finally { setCatLoad(false); }
  };

  useEffect(() => {
    loadProfile();
    loadProducts();
    loadCatRequests();
  }, []);

  // ── Actions ──────────────────────────────────────────────────
  const deleteProduct = async (id) => {
    if (!window.confirm("Soft-delete this product?")) return;
    try {
      await api("DELETE", `/delete/${id}`);
      showToast("Product deactivated.", "success");
      loadProducts(page);
    } catch (err) {
      showToast(err.message, "error");
    }
  };

  const changePage = (p) => { setPage(p); loadProducts(p); };
  const goTo       = (id) => { setActive(id); setSidebar(false); };

  // ── Section renderer ─────────────────────────────────────────
  const renderSection = () => {
    switch (active) {
      case "dashboard":
        return <Dashboard profile={profile} products={products} catRequests={catRequests} onNav={goTo} />;
      case "profile":
        return <ProfileSection existing={profile} onSaved={() => { loadProfile(); goTo("dashboard"); }} showToast={showToast} />;
      case "products":
        return <ProductsSection products={products} loading={prodLoading} page={page} onPageChange={changePage}
                 onDelete={deleteProduct}
                 onAdd={() => { setEditProd(null); goTo("addProduct"); }}
                 onEdit={(p) => { setEditProd(p); goTo("addProduct"); }} />;
      case "addProduct":
        return <ProductForm editProduct={editProduct}
                 onSaved={() => { setEditProd(null); loadProducts(page); goTo("products"); }}
                 showToast={showToast} />;
      case "categories":
        return <CategoryRequests requests={catRequests} loading={catLoading} onRefresh={loadCatRequests} showToast={showToast} />;
      default:
        return null;
    }
  };

  const pendingCat = catRequests.filter(c => c.status === "PENDING").length;
  const topbarSub = {
    dashboard:  "View Analytics",
    profile:    "View Profile",
    products:   "View Products",
    addProduct: "Add Product",
    categories: "Add Category",
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 flex">
      {sidebarOpen && (
        <div className="fixed inset-0 bg-black/40 z-20 lg:hidden" onClick={() => setSidebar(false)} />
      )}

      {/* ── Sidebar ── */}
      <aside className={`fixed top-0 left-0 h-full w-64 bg-white dark:bg-gray-900 border-r border-gray-100 dark:border-gray-800 z-30 flex flex-col transform transition-transform duration-300 ${sidebarOpen ? "translate-x-0" : "-translate-x-full"} lg:translate-x-0 lg:static lg:z-auto`}>

        <div className="px-5 py-5 border-b border-gray-100 dark:border-gray-800 flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-primary-500 to-primary-600 flex items-center justify-center text-white font-bold shadow-md shadow-primary-500/25">A</div>
          <div>
            <p className="text-sm font-bold text-gray-900 dark:text-white">Artisan Panel</p>
            <p className="text-[10px] text-gray-400 dark:text-gray-500 uppercase tracking-wider">Seller Dashboard</p>
          </div>
        </div>

        {profile && (
          <div className="mx-3 mt-4 p-3 rounded-xl bg-primary-50 dark:bg-primary-900/20 border border-primary-100 dark:border-primary-800/30">
            <div className="flex items-center justify-between gap-1">
              <p className="text-sm font-semibold text-primary-800 dark:text-primary-300 truncate">{profile.brandName}</p>
              <Badge color={KYC_COLOR[profile.kycStatus] ?? "blue"}>{profile.kycStatus}</Badge>
            </div>
            <p className="text-[11px] text-primary-600 dark:text-primary-400 truncate mt-0.5">{profile.artisianType}</p>
            {profile.username && <p className="text-[10px] text-primary-500/60 dark:text-primary-500/40 mt-0.5">@{profile.username}</p>}
          </div>
        )}

        <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
          {NAV.map((item) => {
            const on = active === item.id;
            return (
              <button key={item.id}
                onClick={() => { goTo(item.id); if (item.id !== "addProduct") setEditProd(null); }}
                className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all ${on ? "bg-primary-600 text-white shadow-md shadow-primary-500/25" : "text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800"}`}>
                <span className={`w-5 text-center ${on ? "text-white" : "text-gray-400 dark:text-gray-500"}`}>{item.icon}</span>
                {item.label}
                {item.id === "products" && (products?.totalElements ?? 0) > 0 && (
                  <span className={`ml-auto text-[10px] font-bold px-1.5 py-0.5 rounded-md ${on ? "bg-white/20 text-white" : "bg-primary-100 dark:bg-primary-900/30 text-primary-600 dark:text-primary-400"}`}>
                    {products.totalElements}
                  </span>
                )}
                {item.id === "categories" && pendingCat > 0 && (
                  <span className={`ml-auto text-[10px] font-bold px-1.5 py-0.5 rounded-md ${on ? "bg-white/20 text-white" : "bg-amber-100 dark:bg-amber-900/30 text-amber-600 dark:text-amber-400"}`}>
                    {pendingCat}
                  </span>
                )}
              </button>
            );
          })}
        </nav>

        <div className="p-4 border-t border-gray-100 dark:border-gray-800">
          <button onClick={() => { setEditProd(null); goTo("addProduct"); }}
            className="w-full btn-primary py-2.5 text-sm flex items-center justify-center gap-2">
            <span>+</span> Add Product
          </button>
        </div>
      </aside>

      {/* ── Main ── */}
      <div className="flex-1 flex flex-col min-w-0">
        <header className="bg-white dark:bg-gray-900 border-b border-gray-100 dark:border-gray-800 px-5 py-3.5 flex items-center gap-4 sticky top-0 z-10">
          <button onClick={() => setSidebar(true)} className="lg:hidden p-2 rounded-xl text-gray-500 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
          <div>
            <p className="text-sm font-semibold text-gray-800 dark:text-gray-200 capitalize">
              {active === "addProduct" ? (editProduct ? "Edit Product" : "Add Product") : active.replace(/([A-Z])/g, " $1")}
            </p>
            <p className="text-[10px] text-gray-400 dark:text-gray-500 font-mono">{topbarSub[active]}</p>
          </div>
          <div className="ml-auto flex items-center gap-3">
            {profileLoading && <span className="text-xs text-gray-400 animate-pulse">Loading…</span>}
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-primary-500 to-primary-600 flex items-center justify-center text-white text-sm font-bold shadow">
                {profile?.brandName?.[0] ?? "A"}
              </div>
              {profile && (
                <div className="hidden sm:block">
                  <p className="text-sm font-medium text-gray-700 dark:text-gray-300 leading-none">{profile.brandName}</p>
                  <p className="text-[10px] text-gray-400 dark:text-gray-500 mt-0.5">@{profile.username}</p>
                </div>
              )}
            </div>
          </div>
        </header>

        <main className="flex-1 p-5 lg:p-8 overflow-y-auto">
          {renderSection()}
        </main>
      </div>

      <Toast msg={toast.msg} type={toast.type} />
    </div>
  );
}
