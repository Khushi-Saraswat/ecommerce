import { lazy, Suspense } from "react";
import { Toaster } from "react-hot-toast";
import { Route, BrowserRouter as Router, Routes, useLocation } from "react-router-dom";

import Footer from "./components/layout/Footer";
import Navbar from "./components/layout/Navbar";
import LoadingSpinner from "./components/ui/LoadingSpinner";

// Guards
const ProtectedRoute = lazy(() => import("./components/auth/ProtectedRoute"));
const AdminRoute     = lazy(() => import("./components/auth/AdminRoute"));
const ArtisanRoute   = lazy(() => import("./components/auth/ArtisanRoute"));

// Public pages
const Home          = lazy(() => import("./pages/Home"));
const Products      = lazy(() => import("./pages/Products"));
const ProductDetails= lazy(() => import("./pages/ProductDetails"));
const Cart          = lazy(() => import("./pages/Cart"));
const Wishlist      = lazy(() => import("./pages/Wishlist"));
const Checkout      = lazy(() => import("./pages/Checkout"));
const Login         = lazy(() => import("./pages/Login"));
const Register      = lazy(() => import("./pages/Register"));
const Profile       = lazy(() => import("./pages/Profile"));
const Orders        = lazy(() => import("./pages/Orders"));
const OrderDetails  = lazy(() => import("./pages/OrderDetails"));

const ApplyArtisan    = lazy(() => import("./pages/ApplyArtisan"));
const WaitingApproval = lazy(() => import("./pages/WaitingApproval"));

// Admin pages
const AdminDashboard   = lazy(() => import("./pages/admin/Dashboard"));
const AdminProducts    = lazy(() => import("./pages/admin/Products"));
const AdminProductForm = lazy(() => import("./pages/admin/ProductForm"));
const AdminCategories  = lazy(() => import("./pages/admin/Categories"));
const AdminOrders      = lazy(() => import("./pages/admin/Orders"));
const AdminOrderDetails= lazy(() => import("./pages/admin/OrderDetails"));
const AdminUsers       = lazy(() => import("./pages/admin/Users"));

// Artisan — single self-contained panel (handles its own internal navigation)
const ArtisanPanel = lazy(() => import("./pages/artisan/ArtisanPanel"));

// ─── Loaders ─────────────────────────────────────────────────────────────────
const PageLoader = () => (
  <div className="flex items-center justify-center min-h-[60vh]">
    <LoadingSpinner size="lg" />
  </div>
);

// ─── Layout wrapper — hides Navbar & Footer on /artisan routes ────────────────
function Layout() {
  const { pathname } = useLocation();
  const isArtisan = pathname.startsWith("/artisan");

  return (
    <div className="min-h-screen flex flex-col">

      {!isArtisan && <Navbar />}

      <main className="flex-grow">
        <Suspense fallback={<PageLoader />}>
          <Routes>

            {/* ---------- PUBLIC ---------- */}
            <Route path="/"               element={<Home />} />
            <Route path="/products"       element={<Products />} />
            <Route path="/products/:id"   element={<ProductDetails />} />
            <Route path="/wishlist"       element={<Wishlist />} />
            <Route path="/login"          element={<Login />} />
            <Route path="/register"       element={<Register />} />

            {/* ---------- USER PROTECTED ---------- */}
            <Route path="/cart"           element={<ProtectedRoute><Cart /></ProtectedRoute>} />
            <Route path="/checkout"       element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
            <Route path="/profile"        element={<ProtectedRoute><Profile /></ProtectedRoute>} />
            <Route path="/orders"         element={<ProtectedRoute><Orders /></ProtectedRoute>} />
            <Route path="/orders/:id"     element={<ProtectedRoute><OrderDetails /></ProtectedRoute>} />

            {/* ---------- ADMIN ---------- */}
            <Route path="/admin"                    element={<AdminRoute><AdminDashboard /></AdminRoute>} />
            <Route path="/admin/products"           element={<AdminRoute><AdminProducts /></AdminRoute>} />
            <Route path="/admin/products/new"       element={<AdminRoute><AdminProductForm /></AdminRoute>} />
            <Route path="/admin/products/:id/edit"  element={<AdminRoute><AdminProductForm /></AdminRoute>} />
            <Route path="/admin/categories"         element={<AdminRoute><AdminCategories /></AdminRoute>} />
            <Route path="/admin/orders"             element={<AdminRoute><AdminOrders /></AdminRoute>} />
            <Route path="/admin/orders/:id"         element={<AdminRoute><AdminOrderDetails /></AdminRoute>} />
            <Route path="/admin/users"              element={<AdminRoute><AdminUsers /></AdminRoute>} />

            {/* ---------- ARTISAN APPLICATION FLOW ---------- */}
            <Route path="/apply-artisan"    element={<ProtectedRoute><ApplyArtisan /></ProtectedRoute>} />
            <Route path="/waiting-approval" element={<ProtectedRoute><WaitingApproval /></ProtectedRoute>} />

            {/* ---------- ARTISAN PANEL ----------
                ArtisanPanel manages its own sidebar navigation internally.
                No child routes needed — /* catches /artisan and any sub-path.       */}
            <Route
              path="/artisan/*"
              element={
                <ArtisanRoute>
                  <ArtisanPanel />
                </ArtisanRoute>
              }
            />

          </Routes>
        </Suspense>
      </main>

      {!isArtisan && <Footer />}

      <Toaster position="top-right" />

    </div>
  );
}

// ─── App ──────────────────────────────────────────────────────────────────────
function App() {
  return (
    <Router>
      <Layout />
    </Router>
  );
}

export default App;
