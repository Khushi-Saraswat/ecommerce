import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  ShoppingBagIcon, 
  UsersIcon, 
  CurrencyRupeeIcon,
  CubeIcon,
  ArrowTrendingUpIcon,
  ArrowTrendingDownIcon
} from '@heroicons/react/24/outline';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import { adminAPI } from '../../services/api';

export default function AdminDashboard() {
  const [stats, setStats] = useState({
    totalRevenue: 0,
    totalOrders: 0,
    totalProducts: 0,
    totalUsers: 0,
  });
  const [recentOrders, setRecentOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch stats and orders separately to handle partial failures
        const statsRes = await adminAPI.getDashboardStats();
        console.log('Dashboard stats response:', statsRes.data);
        if (statsRes.data?.data) {
          setStats(statsRes.data.data);
        }
      } catch (error) {
        console.error('Failed to fetch dashboard stats:', error);
      }
      
      try {
        const ordersRes = await adminAPI.getOrders({ size: 5, sortBy: 'createdAt', sortDir: 'desc' });
        console.log('Orders response:', ordersRes.data);
        setRecentOrders(ordersRes.data?.data?.content || []);
      } catch (error) {
        console.error('Failed to fetch orders:', error);
      }
      
      setIsLoading(false);
    };
    fetchData();
  }, []);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  const statCards = [
    {
      title: 'Total Revenue',
      value: `₹${(stats?.totalRevenue || 0).toLocaleString()}`,
      icon: CurrencyRupeeIcon,
      change: '+12%',
      isPositive: true,
      color: 'bg-green-500',
    },
    {
      title: 'Total Orders',
      value: stats?.totalOrders || 0,
      icon: ShoppingBagIcon,
      change: '+8%',
      isPositive: true,
      color: 'bg-blue-500',
    },
    {
      title: 'Total Products',
      value: stats?.totalProducts || 0,
      icon: CubeIcon,
      change: '+5%',
      isPositive: true,
      color: 'bg-purple-500',
    },
    {
      title: 'Total Users',
      value: stats?.totalUsers || 0,
      icon: UsersIcon,
      change: '+15%',
      isPositive: true,
      color: 'bg-orange-500',
    },
  ];

  const getStatusBadge = (status) => {
    const styles = {
      PENDING: 'bg-yellow-100 text-yellow-800',
      CONFIRMED: 'bg-blue-100 text-blue-800',
      PROCESSING: 'bg-blue-100 text-blue-800',
      SHIPPED: 'bg-purple-100 text-purple-800',
      DELIVERED: 'bg-green-100 text-green-800',
      CANCELLED: 'bg-red-100 text-red-800',
    };
    return styles[status] || 'bg-gray-100 text-gray-800';
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div className="mb-8">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-900 dark:text-white">
          Admin Dashboard
        </h1>
        <p className="text-gray-500 dark:text-gray-400 mt-1">
          Welcome back! Here's an overview of your store.
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {statCards.map((stat, index) => (
          <div key={index} className="card">
            <div className="flex items-center justify-between mb-4">
              <div className={`p-3 rounded-lg ${stat.color}`}>
                <stat.icon className="h-6 w-6 text-white" />
              </div>
              <span className={`flex items-center text-sm font-medium ${
                stat.isPositive ? 'text-green-600' : 'text-red-600'
              }`}>
                {stat.isPositive ? (
                  <ArrowTrendingUpIcon className="h-4 w-4 mr-1" />
                ) : (
                  <ArrowTrendingDownIcon className="h-4 w-4 mr-1" />
                )}
                {stat.change}
              </span>
            </div>
            <h3 className="text-2xl font-bold text-gray-900 dark:text-white">
              {stat.value}
            </h3>
            <p className="text-gray-500 dark:text-gray-400">{stat.title}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Recent Orders */}
        <div className="card">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-lg font-semibold">Recent Orders</h2>
            <Link
              to="/admin/orders"
              className="text-primary-600 hover:text-primary-700 text-sm font-medium"
            >
              View All
            </Link>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="text-left text-gray-500 dark:text-gray-400 text-sm">
                  <th className="pb-3">Order</th>
                  <th className="pb-3">Customer</th>
                  <th className="pb-3">Amount</th>
                  <th className="pb-3">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y dark:divide-gray-700">
                {recentOrders.length === 0 ? (
                  <tr>
                    <td colSpan="4" className="py-8 text-center text-gray-500">
                      No recent orders
                    </td>
                  </tr>
                ) : recentOrders.map((order) => (
                  <tr key={order.id}>
                    <td className="py-3">
                      <Link
                        to={`/admin/orders/${order.id}`}
                        className="text-primary-600 hover:text-primary-700 font-medium"
                      >
                        #{order.orderNumber || order.id?.substring(0, 8).toUpperCase()}
                      </Link>
                    </td>
                    <td className="py-3 text-gray-600 dark:text-gray-400">
                      {order.userName || order.userEmail || 'Customer'}
                    </td>
                    <td className="py-3 font-medium">
                      ₹{(order.totalAmount || 0).toLocaleString()}
                    </td>
                    <td className="py-3">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusBadge(order.orderStatus || order.status)}`}>
                        {order.orderStatus || order.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-6">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-4">
            <Link
              to="/admin/products/new"
              className="p-4 border rounded-lg hover:border-primary-600 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors text-center"
            >
              <CubeIcon className="h-8 w-8 mx-auto mb-2 text-primary-600" />
              <span className="font-medium">Add Product</span>
            </Link>
            <Link
              to="/admin/categories"
              className="p-4 border rounded-lg hover:border-primary-600 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors text-center"
            >
              <CubeIcon className="h-8 w-8 mx-auto mb-2 text-primary-600" />
              <span className="font-medium">Manage Categories</span>
            </Link>
            <Link
              to="/admin/orders"
              className="p-4 border rounded-lg hover:border-primary-600 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors text-center"
            >
              <ShoppingBagIcon className="h-8 w-8 mx-auto mb-2 text-primary-600" />
              <span className="font-medium">Manage Orders</span>
            </Link>
            <Link
              to="/admin/users"
              className="p-4 border rounded-lg hover:border-primary-600 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors text-center"
            >
              <UsersIcon className="h-8 w-8 mx-auto mb-2 text-primary-600" />
              <span className="font-medium">Manage Users</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
