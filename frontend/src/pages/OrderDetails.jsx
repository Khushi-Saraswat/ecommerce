import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { 
  TruckIcon, 
  CheckCircleIcon,
  ClockIcon,
  ArrowLeftIcon
} from '@heroicons/react/24/outline';
import LoadingSpinner from '../components/ui/LoadingSpinner';
import { ordersAPI } from '../services/api';

export default function OrderDetails() {
  const { id } = useParams();
  const [order, setOrder] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const response = await ordersAPI.getById(id);
        setOrder(response.data.data);
      } catch (error) {
        console.error('Failed to fetch order:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchOrder();
  }, [id]);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!order) {
    return (
      <div className="text-center py-20">
        <p className="text-gray-500">Order not found</p>
        <Link to="/orders" className="btn-primary mt-4">
          Back to Orders
        </Link>
      </div>
    );
  }

  const steps = [
    { status: 'PENDING', label: 'Order Placed', icon: ClockIcon },
    { status: 'CONFIRMED', label: 'Confirmed', icon: CheckCircleIcon },
    { status: 'PROCESSING', label: 'Processing', icon: ClockIcon },
    { status: 'SHIPPED', label: 'Shipped', icon: TruckIcon },
    { status: 'DELIVERED', label: 'Delivered', icon: CheckCircleIcon },
  ];

  const getCurrentStep = () => {
    const statusOrder = ['PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED'];
    return statusOrder.indexOf(order.orderStatus);
  };

  const currentStep = getCurrentStep();

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <Link
        to="/orders"
        className="inline-flex items-center text-gray-600 hover:text-primary-600 mb-6"
      >
        <ArrowLeftIcon className="h-4 w-4 mr-2" />
        Back to Orders
      </Link>

      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-gray-900 dark:text-white">
            Order #{order.orderNumber}
          </h1>
          <p className="text-gray-500 dark:text-gray-400">
            Placed on {new Date(order.createdAt).toLocaleDateString()}
          </p>
        </div>
      </div>

      {/* Order Status Timeline */}
      {order.orderStatus !== 'CANCELLED' && (
        <div className="card mb-8">
          <h2 className="text-lg font-semibold mb-6">Order Status</h2>
          <div className="flex items-center justify-between relative">
            <div className="absolute left-0 right-0 top-5 h-1 bg-gray-200 dark:bg-gray-700">
              <div
                className="h-full bg-primary-600 transition-all"
                style={{ width: `${(currentStep / (steps.length - 1)) * 100}%` }}
              />
            </div>
            {steps.map((step, index) => (
              <div
                key={step.status}
                className={`relative flex flex-col items-center ${
                  index <= currentStep ? 'text-primary-600' : 'text-gray-400'
                }`}
              >
                <div
                  className={`w-10 h-10 rounded-full flex items-center justify-center ${
                    index <= currentStep
                      ? 'bg-primary-600 text-white'
                      : 'bg-gray-200 dark:bg-gray-700'
                  }`}
                >
                  <step.icon className="h-5 w-5" />
                </div>
                <span className="mt-2 text-xs font-medium text-center hidden sm:block">
                  {step.label}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {order.orderStatus === 'CANCELLED' && (
        <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-4 mb-8">
          <p className="text-red-600 dark:text-red-400 font-medium">
            This order has been cancelled.
          </p>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-6">
          {/* Order Items */}
          <div className="card">
            <h2 className="text-lg font-semibold mb-4">Order Items</h2>
            <div className="divide-y dark:divide-gray-700">
              {order.items?.map((item) => (
                <div key={item.productId} className="flex gap-4 py-4">
                  <div className="w-20 h-20 bg-gray-100 dark:bg-gray-700 rounded-lg overflow-hidden flex-shrink-0">
                    <img
                      src={item.productImage || 'https://via.placeholder.com/80'}
                      alt={item.productName}
                      className="w-full h-full object-cover"
                    />
                  </div>
                  <div className="flex-1">
                    <Link
                      to={`/products/${item.productId}`}
                      className="font-medium hover:text-primary-600"
                    >
                      {item.productName}
                    </Link>
                    <p className="text-sm text-gray-500 mt-1">
                      Quantity: {item.quantity}
                    </p>
                    <p className="text-sm text-gray-500">
                      Price: ₹{item.price.toLocaleString()}
                    </p>
                  </div>
                  <p className="font-medium">
                    ₹{(item.price * item.quantity).toLocaleString()}
                  </p>
                </div>
              ))}
            </div>
          </div>

          {/* Shipping Address */}
          <div className="card">
            <h2 className="text-lg font-semibold mb-4">Shipping Address</h2>
            <p className="text-gray-600 dark:text-gray-400">
              {order.shippingAddress?.fullName}<br />
              {order.shippingAddress?.street}<br />
              {order.shippingAddress?.city}, {order.shippingAddress?.state} {order.shippingAddress?.zipCode}<br />
              {order.shippingAddress?.country}<br />
              Phone: {order.shippingAddress?.phone}
            </p>
          </div>
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="card sticky top-24">
            <h2 className="text-lg font-semibold mb-4">Order Summary</h2>
            <div className="space-y-3 mb-6">
              <div className="flex justify-between text-gray-600 dark:text-gray-400">
                <span>Subtotal</span>
                <span>₹{order.subtotal?.toLocaleString()}</span>
              </div>
              <div className="flex justify-between text-gray-600 dark:text-gray-400">
                <span>Shipping</span>
                <span>{order.shippingCost === 0 ? 'FREE' : `₹${order.shippingCost}`}</span>
              </div>
              {order.discount > 0 && (
                <div className="flex justify-between text-green-600">
                  <span>Discount</span>
                  <span>-₹{order.discount.toLocaleString()}</span>
                </div>
              )}
              <div className="border-t dark:border-gray-700 pt-3">
                <div className="flex justify-between font-semibold text-lg">
                  <span>Total</span>
                  <span>₹{order.totalAmount?.toLocaleString()}</span>
                </div>
              </div>
            </div>

            <div className="border-t dark:border-gray-700 pt-4">
              <h3 className="font-medium mb-2">Payment Method</h3>
              <p className="text-gray-600 dark:text-gray-400">
                {order.paymentMethod === 'COD' && 'Cash on Delivery'}
                {order.paymentMethod === 'CARD' && 'Credit/Debit Card'}
                {order.paymentMethod === 'UPI' && 'UPI Payment'}
              </p>
              <p className={`text-sm mt-1 ${
                order.paymentStatus === 'PAID' ? 'text-green-600' : 'text-yellow-600'
              }`}>
                {order.paymentStatus}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
