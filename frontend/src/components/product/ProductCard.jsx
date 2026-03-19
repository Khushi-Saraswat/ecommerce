import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  ShoppingCartIcon, 
  HeartIcon, 
  StarIcon,
  EyeIcon,
  CheckIcon
} from '@heroicons/react/24/outline';
import { HeartIcon as HeartIconSolid, StarIcon as StarIconSolid } from '@heroicons/react/24/solid';
import { useCartStore } from '../../store/cartStore';
import { useAuthStore } from '../../store/authStore';
import { useWishlistStore } from '../../store/wishlistStore';

export default function ProductCard({ product, showQuickView = true }) {
  const { addToCart, isLoading } = useCartStore();
  const { isAuthenticated } = useAuthStore();
  const { addToWishlist, removeFromWishlist, isInWishlist } = useWishlistStore();
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);
  const [imageLoaded, setImageLoaded] = useState(false);
  const [addedToCart, setAddedToCart] = useState(false);

  const inWishlist = isInWishlist(product.id);

  const handleAddToCart = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    
    const result = await addToCart(product.id, 1);
    if (result.success) {
      setAddedToCart(true);
      setTimeout(() => setAddedToCart(false), 2000);
    }
  };

  const handleWishlistClick = (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (inWishlist) {
      removeFromWishlist(product.id);
    } else {
      addToWishlist(product);
    }
  };

  const renderStars = (rating) => {
    const stars = [];
    const fullStars = Math.floor(rating || 0);
    
    for (let i = 0; i < 5; i++) {
      if (i < fullStars) {
        stars.push(<StarIconSolid key={i} className="h-3.5 w-3.5 text-yellow-400" />);
      } else {
        stars.push(<StarIcon key={i} className="h-3.5 w-3.5 text-gray-300 dark:text-gray-600" />);
      }
    }
    return stars;
  };

  const discountPercentage = product.discountPrice 
    ? Math.round((1 - product.discountPrice / product.price) * 100)
    : 0;

  const isOutOfStock = product.stockQuantity === 0;
  const isLowStock = product.stockQuantity > 0 && product.stockQuantity <= 5;

  return (
    <Link 
      to={`/products/${product.id}`} 
      className="group block"
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div className="card-hover overflow-hidden">
        {/* Image Container */}
        <div className="relative aspect-square bg-gradient-to-br from-gray-100 to-gray-50 dark:from-gray-800 dark:to-gray-700 overflow-hidden">
          {/* Skeleton while loading */}
          {!imageLoaded && (
            <div className="absolute inset-0 skeleton" />
          )}
          
          <img
            src={product.images?.[0] || 'https://via.placeholder.com/400x400?text=No+Image'}
            alt={product.name}
            className={`w-full h-full object-cover transition-all duration-500 
                       ${isHovered ? 'scale-110' : 'scale-100'}
                       ${imageLoaded ? 'opacity-100' : 'opacity-0'}`}
            onLoad={() => setImageLoaded(true)}
            loading="lazy"
          />
          
          {/* Overlay on out of stock */}
          {isOutOfStock && (
            <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
              <span className="bg-white text-gray-900 px-4 py-2 rounded-full font-semibold text-sm">
                Out of Stock
              </span>
            </div>
          )}
          
          {/* Badges */}
          <div className="absolute top-3 left-3 flex flex-col gap-2">
            {product.featured && (
              <span className="bg-gradient-to-r from-primary-600 to-primary-500 text-white text-xs font-semibold px-2.5 py-1 rounded-full shadow-lg">
                Featured
              </span>
            )}
            {discountPercentage > 0 && (
              <span className="bg-gradient-to-r from-red-600 to-red-500 text-white text-xs font-semibold px-2.5 py-1 rounded-full shadow-lg">
                -{discountPercentage}%
              </span>
            )}
            {isLowStock && !isOutOfStock && (
              <span className="bg-gradient-to-r from-orange-500 to-amber-500 text-white text-xs font-semibold px-2.5 py-1 rounded-full shadow-lg">
                Only {product.stockQuantity} left
              </span>
            )}
          </div>

          {/* Quick Action Buttons */}
          <div className={`absolute top-3 right-3 flex flex-col gap-2 transition-all duration-300 ${isHovered ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-2'}`}>
            <button 
              onClick={handleWishlistClick}
              className={`p-2.5 rounded-xl shadow-lg backdrop-blur-sm transition-all duration-200 
                         ${inWishlist 
                           ? 'bg-red-500 text-white' 
                           : 'bg-white/90 dark:bg-gray-800/90 text-gray-600 dark:text-gray-300 hover:bg-red-500 hover:text-white'}`}
            >
              {inWishlist ? (
                <HeartIconSolid className="h-5 w-5" />
              ) : (
                <HeartIcon className="h-5 w-5" />
              )}
            </button>
            
            {showQuickView && (
              <button 
                onClick={(e) => {
                  e.preventDefault();
                  e.stopPropagation();
                  // Could open quick view modal
                }}
                className="p-2.5 bg-white/90 dark:bg-gray-800/90 rounded-xl shadow-lg backdrop-blur-sm 
                          text-gray-600 dark:text-gray-300 hover:bg-primary-500 hover:text-white transition-all duration-200"
              >
                <EyeIcon className="h-5 w-5" />
              </button>
            )}
          </div>

          {/* Add to Cart Button */}
          <div className={`absolute bottom-0 left-0 right-0 p-4 bg-gradient-to-t from-black/80 via-black/50 to-transparent 
                          transition-all duration-300 ${isHovered && !isOutOfStock ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'}`}>
            <button
              onClick={handleAddToCart}
              disabled={isLoading || isOutOfStock}
              className={`w-full py-2.5 rounded-xl font-semibold flex items-center justify-center gap-2 
                         transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed
                         ${addedToCart 
                           ? 'bg-green-500 text-white' 
                           : 'bg-white text-gray-900 hover:bg-primary-500 hover:text-white'}`}
            >
              {addedToCart ? (
                <>
                  <CheckIcon className="h-5 w-5" />
                  Added!
                </>
              ) : (
                <>
                  <ShoppingCartIcon className="h-5 w-5" />
                  Add to Cart
                </>
              )}
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="p-4">
          {/* Category */}
          <p className="text-xs text-primary-600 dark:text-primary-400 font-semibold uppercase tracking-wide mb-1.5">
            {product.categoryName || 'Uncategorized'}
          </p>

          {/* Name */}
          <h3 className="font-semibold text-gray-900 dark:text-white mb-2 line-clamp-2 
                        group-hover:text-primary-600 transition-colors duration-200 min-h-[2.5rem]">
            {product.name}
          </h3>

          {/* Rating */}
          <div className="flex items-center gap-2 mb-3">
            <div className="flex">
              {renderStars(product.averageRating)}
            </div>
            <span className="text-xs text-gray-500 dark:text-gray-400">
              ({product.reviewCount || 0})
            </span>
          </div>

          {/* Price */}
          <div className="flex items-center gap-2 flex-wrap">
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
                ₹{product.price?.toLocaleString() || '0'}
              </span>
            )}
          </div>

          {/* Brand */}
          {product.brand && (
            <p className="text-xs text-gray-500 dark:text-gray-400 mt-2">
              by <span className="font-medium">{product.brand}</span>
            </p>
          )}
        </div>
      </div>
    </Link>
  );
}
