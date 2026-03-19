import { Link } from 'react-router-dom';
import { HeartIcon, ShoppingCartIcon, TrashIcon } from '@heroicons/react/24/outline';
import { useWishlistStore } from '../store/wishlistStore';
import { useCartStore } from '../store/cartStore';
import { useAuthStore } from '../store/authStore';
import EmptyState from '../components/ui/EmptyState';

export default function Wishlist() {
  const { items, removeFromWishlist, clearWishlist } = useWishlistStore();
  const { addToCart, isLoading } = useCartStore();
  const { isAuthenticated } = useAuthStore();

  const handleMoveToCart = async (product) => {
    await addToCart(product.id, 1);
    removeFromWishlist(product.id);
  };

  if (items.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <EmptyState
          icon="wishlist"
          title="Your wishlist is empty"
          description="Save items you love by clicking the heart icon on any product."
          actionTo="/products"
          actionLabel="Browse Products"
        />
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-gray-900 dark:text-white">
            My Wishlist
          </h1>
          <p className="text-gray-500 dark:text-gray-400 mt-1">
            {items.length} {items.length === 1 ? 'item' : 'items'} saved
          </p>
        </div>
        
        {items.length > 0 && (
          <button
            onClick={() => {
              if (window.confirm('Are you sure you want to clear your wishlist?')) {
                clearWishlist();
              }
            }}
            className="text-red-500 hover:text-red-600 text-sm font-medium"
          >
            Clear Wishlist
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {items.map((product) => (
          <div key={product.id} className="card-hover overflow-hidden group">
            {/* Image */}
            <Link to={`/products/${product.id}`} className="block">
              <div className="relative aspect-square bg-gray-100 dark:bg-gray-700 overflow-hidden">
                <img
                  src={product.images?.[0] || 'https://via.placeholder.com/300'}
                  alt={product.name}
                  className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                />
                
                {product.discountPrice && (
                  <span className="absolute top-3 left-3 bg-red-500 text-white text-xs font-semibold px-2.5 py-1 rounded-full">
                    -{Math.round((1 - product.discountPrice / product.price) * 100)}%
                  </span>
                )}
              </div>
            </Link>

            {/* Content */}
            <div className="p-4">
              <p className="text-xs text-primary-600 dark:text-primary-400 font-semibold uppercase tracking-wide mb-1">
                {product.categoryName}
              </p>
              
              <Link 
                to={`/products/${product.id}`}
                className="font-semibold text-gray-900 dark:text-white hover:text-primary-600 line-clamp-2 mb-2"
              >
                {product.name}
              </Link>

              <div className="flex items-center gap-2 mb-4">
                {product.discountPrice ? (
                  <>
                    <span className="text-lg font-bold text-gray-900 dark:text-white">
                      ₹{product.discountPrice.toLocaleString()}
                    </span>
                    <span className="text-sm text-gray-400 line-through">
                      ₹{product.price.toLocaleString()}
                    </span>
                  </>
                ) : (
                  <span className="text-lg font-bold text-gray-900 dark:text-white">
                    ₹{product.price?.toLocaleString()}
                  </span>
                )}
              </div>

              {/* Actions */}
              <div className="flex gap-2">
                <button
                  onClick={() => handleMoveToCart(product)}
                  disabled={isLoading || !isAuthenticated}
                  className="flex-1 btn-primary btn-sm flex items-center justify-center gap-2"
                >
                  <ShoppingCartIcon className="h-4 w-4" />
                  Add to Cart
                </button>
                
                <button
                  onClick={() => removeFromWishlist(product.id)}
                  className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 
                           rounded-lg transition-colors"
                >
                  <TrashIcon className="h-5 w-5" />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
