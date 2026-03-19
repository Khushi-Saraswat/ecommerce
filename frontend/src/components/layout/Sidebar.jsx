
import { Link } from "react-router-dom";


export default function Sidebar() {
    return (
    <div className="w-64 bg-gray-900 text-white p-6">
      <h2 className="text-xl font-bold mb-6">Artisan Panel</h2>
      <ul className="space-y-4">
        <li><Link to="dashboard">Dashboard</Link></li>
        <li><Link to="profile">Profile</Link></li>
        <li><Link to="products">Products</Link></li>
        <li><Link to="add-product">Add Product</Link></li>
        <li><Link to="category-requests">Category Requests</Link></li>
      </ul>
    </div>
  );
}
