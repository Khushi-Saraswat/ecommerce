import { Menu, Transition } from '@headlessui/react';
import {
  ArrowRightOnRectangleIcon,
  Bars3Icon,
  ChevronDownIcon,
  HeartIcon,
  MagnifyingGlassIcon,
  MoonIcon,
  ShoppingBagIcon,
  ShoppingCartIcon,
  Squares2X2Icon,
  SunIcon,
  UserIcon,
  XMarkIcon
} from '@heroicons/react/24/outline';
import { Fragment, useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../store/authStore';
import { useCartStore } from '../../store/cartStore';
import { useWishlistStore } from '../../store/wishlistStore';
import Top from './Top';


export default function Navbar() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [isDark, setIsDark] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [isArtisanModalOpen, setIsArtisanModalOpen] = useState(false); // ← NEW

  const { isAuthenticated, user, logout, isAdmin } = useAuthStore();
  const { getItemCount, fetchCart } = useCartStore();
  const { getItemCount: getWishlistCount, fetchWishlist } = useWishlistStore();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const handleScroll = () => setIsScrolled(window.scrollY > 10);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
      fetchWishlist();
    }
  }, [isAuthenticated, fetchCart, fetchWishlist]);

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      setIsDark(true);
      document.documentElement.classList.add('dark');
    }
  }, []);

  useEffect(() => {
    if (isDark) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    }
  }, [isDark]);

  useEffect(() => {
    setIsMenuOpen(false);
  }, [location]);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/products?search=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
      setIsSearchFocused(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const cartCount = getItemCount();
  const wishlistCount = getWishlistCount();

  const navLinks = [
    { name: 'Home', path: '/' },
    { name: 'Products', path: '/products' },
    { name: 'Featured', path: '/products?featured=true' },
  ];

  return (
    <>
      {/* ── Top Bar ── */}
      <Top/>

      {/* ── Main Nav ── */}
      <nav className={`sticky top-0 z-50 transition-all duration-300 ${
        isScrolled
          ? 'bg-white/95 dark:bg-gray-900/95 backdrop-blur-lg shadow-soft'
          : 'bg-white dark:bg-gray-900'
      }`}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16 lg:h-18">
            {/* Logo */}
            <Link to="/" className="flex items-center gap-3 flex-shrink-0">
              <div className="w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-500 rounded-xl flex items-center justify-center shadow-lg shadow-primary-500/25">
                <span className="text-white font-bold text-xl">S</span>
              </div>
              <span className="text-xl font-display font-bold bg-gradient-to-r from-gray-900 to-gray-600 dark:from-white dark:to-gray-300 bg-clip-text text-transparent">
                ShopEase
              </span>
            </Link>

            {/* Desktop Navigation */}
            <div className="hidden lg:flex items-center gap-1">
              {navLinks.map((link) => (
                <Link
                  key={link.path}
                  to={link.path}
                  className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                    location.pathname === link.path
                      ? 'text-primary-600 bg-primary-50 dark:bg-primary-900/20'
                      : 'text-gray-600 dark:text-gray-300 hover:text-primary-600 hover:bg-gray-100 dark:hover:bg-gray-800'
                  }`}
                >
                  {link.name}
                </Link>
              ))}
            </div>

            {/* Search Bar - Desktop */}
            <form onSubmit={handleSearch} className="hidden md:flex flex-1 max-w-md mx-8">
              <div className={`relative w-full transition-all duration-300 ${isSearchFocused ? 'scale-105' : ''}`}>
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onFocus={() => setIsSearchFocused(true)}
                  onBlur={() => setIsSearchFocused(false)}
                  placeholder="Search products..."
                  className={`w-full pl-11 pr-4 py-2.5 rounded-xl border transition-all duration-300
                           bg-gray-50 dark:bg-gray-800 
                           ${isSearchFocused
                             ? 'border-primary-500 ring-2 ring-primary-500/20'
                             : 'border-gray-200 dark:border-gray-700 hover:border-gray-300 dark:hover:border-gray-600'
                           }
                           focus:outline-none dark:text-white placeholder-gray-400`}
                />
                <MagnifyingGlassIcon className={`absolute left-3.5 top-1/2 -translate-y-1/2 h-5 w-5 transition-colors ${
                  isSearchFocused ? 'text-primary-500' : 'text-gray-400'
                }`} />
              </div>
            </form>

            {/* Right Side Actions */}
            <div className="flex items-center gap-1 sm:gap-2">
              {/* Theme Toggle */}
              <button
                onClick={() => setIsDark(!isDark)}
                className="p-2.5 text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200 
                          hover:bg-gray-100 dark:hover:bg-gray-800 rounded-xl transition-all duration-200"
                aria-label="Toggle theme"
              >
                {isDark ? <SunIcon className="h-5 w-5" /> : <MoonIcon className="h-5 w-5" />}
              </button>

              {/* Wishlist */}
              <Link
                to="/wishlist"
                className="relative p-2.5 text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200 
                          hover:bg-gray-100 dark:hover:bg-gray-800 rounded-xl transition-all duration-200 hidden sm:flex"
              >
                <HeartIcon className="h-5 w-5" />
                {wishlistCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 bg-red-500 text-white text-[10px] font-bold 
                                 w-4 h-4 flex items-center justify-center rounded-full animate-scale-in">
                    {wishlistCount > 9 ? '9+' : wishlistCount}
                  </span>
                )}
              </Link>

              {/* Cart */}
              <Link
                to="/cart"
                className="relative p-2.5 text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200 
                          hover:bg-gray-100 dark:hover:bg-gray-800 rounded-xl transition-all duration-200"
              >
                <ShoppingCartIcon className="h-5 w-5" />
                {cartCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 bg-primary-600 text-white text-[10px] font-bold 
                                 w-4 h-4 flex items-center justify-center rounded-full animate-scale-in">
                    {cartCount > 9 ? '9+' : cartCount}
                  </span>
                )}
              </Link>

              {/* User Menu - Desktop */}
              {isAuthenticated ? (
                <Menu as="div" className="relative hidden md:block">
                  <Menu.Button className="flex items-center gap-2 p-2 text-gray-600 dark:text-gray-300 
                                         hover:bg-gray-100 dark:hover:bg-gray-800 rounded-xl transition-colors">
                    <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-600 rounded-lg 
                                  flex items-center justify-center text-white font-semibold text-sm shadow-md">
                      {user?.firstName?.[0] || 'U'}
                    </div>
                    <span className="text-sm font-medium hidden lg:block">{user?.firstName}</span>
                    <ChevronDownIcon className="h-4 w-4 hidden lg:block" />
                  </Menu.Button>

                  <Transition
                    as={Fragment}
                    enter="transition ease-out duration-200"
                    enterFrom="transform opacity-0 scale-95"
                    enterTo="transform opacity-100 scale-100"
                    leave="transition ease-in duration-150"
                    leaveFrom="transform opacity-100 scale-100"
                    leaveTo="transform opacity-0 scale-95"
                  >
                    <Menu.Items className="absolute right-0 mt-2 w-56 bg-white dark:bg-gray-800 rounded-xl 
                                          shadow-soft-lg border border-gray-100 dark:border-gray-700 
                                          py-2 focus:outline-none">
                      <div className="px-4 py-3 border-b border-gray-100 dark:border-gray-700">
                        <p className="text-sm font-semibold text-gray-900 dark:text-white">
                          {user?.firstName} {user?.lastName}
                        </p>
                        <p className="text-xs text-gray-500 dark:text-gray-400 truncate">{user?.email}</p>
                      </div>

                      <Menu.Item>
                        {({ active }) => (
                          <Link to="/profile" className={`flex items-center gap-3 px-4 py-2.5 text-sm ${active ? 'bg-gray-50 dark:bg-gray-700/50' : ''} text-gray-700 dark:text-gray-300`}>
                            <UserIcon className="h-4 w-4" /> My Profile
                          </Link>
                        )}
                      </Menu.Item>

                      <Menu.Item>
                        {({ active }) => (
                          <Link to="/orders" className={`flex items-center gap-3 px-4 py-2.5 text-sm ${active ? 'bg-gray-50 dark:bg-gray-700/50' : ''} text-gray-700 dark:text-gray-300`}>
                            <ShoppingBagIcon className="h-4 w-4" /> My Orders
                          </Link>
                        )}
                      </Menu.Item>

                      <Menu.Item>
                        {({ active }) => (
                          <Link to="/wishlist" className={`flex items-center gap-3 px-4 py-2.5 text-sm ${active ? 'bg-gray-50 dark:bg-gray-700/50' : ''} text-gray-700 dark:text-gray-300`}>
                            <HeartIcon className="h-4 w-4" /> Wishlist
                            {wishlistCount > 0 && (
                              <span className="ml-auto bg-gray-100 dark:bg-gray-700 px-2 py-0.5 rounded-full text-xs">{wishlistCount}</span>
                            )}
                          </Link>
                        )}
                      </Menu.Item>

                      {isAdmin() && (
                        <>
                          <div className="my-2 border-t border-gray-100 dark:border-gray-700" />
                          <Menu.Item>
                            {({ active }) => (
                              <Link to="/admin" className={`flex items-center gap-3 px-4 py-2.5 text-sm ${active ? 'bg-primary-50 dark:bg-primary-900/20' : ''} text-primary-600 dark:text-primary-400 font-medium`}>
                                <Squares2X2Icon className="h-4 w-4" /> Admin Dashboard
                              </Link>
                            )}
                          </Menu.Item>
                        </>
                      )}

                      <div className="my-2 border-t border-gray-100 dark:border-gray-700" />

                      <Menu.Item>
                        {({ active }) => (
                          <button onClick={handleLogout} className={`flex items-center gap-3 px-4 py-2.5 text-sm w-full ${active ? 'bg-red-50 dark:bg-red-900/20' : ''} text-red-600 dark:text-red-400`}>
                            <ArrowRightOnRectangleIcon className="h-4 w-4" /> Logout
                          </button>
                        )}
                      </Menu.Item>
                    </Menu.Items>
                  </Transition>
                </Menu>
              ) : (
                <div className="hidden md:flex items-center gap-2">
                  <Link to="/login" className="px-4 py-2 text-sm font-medium text-gray-600 dark:text-gray-300 hover:text-primary-600 transition-colors">
                    Login
                  </Link>
                  <Link to="/register" className="btn-primary btn-sm">Sign Up</Link>
                </div>
              )}

              {/* Mobile Menu Button */}
              <button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                className="md:hidden p-2.5 text-gray-500 dark:text-gray-400 hover:bg-gray-100 
                          dark:hover:bg-gray-800 rounded-xl transition-colors"
              >
                {isMenuOpen ? <XMarkIcon className="h-6 w-6" /> : <Bars3Icon className="h-6 w-6" />}
              </button>
            </div>
          </div>

          {/* Mobile Menu */}
          <Transition
            show={isMenuOpen}
            enter="transition ease-out duration-200"
            enterFrom="opacity-0 -translate-y-2"
            enterTo="opacity-100 translate-y-0"
            leave="transition ease-in duration-150"
            leaveFrom="opacity-100 translate-y-0"
            leaveTo="opacity-0 -translate-y-2"
          >
            <div className="md:hidden py-4 border-t border-gray-100 dark:border-gray-800">
              {/* Mobile Search */}
              <form onSubmit={handleSearch} className="mb-4">
                <div className="relative">
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder="Search products..."
                    className="w-full pl-11 pr-4 py-3 border border-gray-200 dark:border-gray-700 rounded-xl 
                             bg-gray-50 dark:bg-gray-800 dark:text-white focus:outline-none 
                             focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500"
                  />
                  <MagnifyingGlassIcon className="absolute left-3.5 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
                </div>
              </form>

              <div className="space-y-1">
                {navLinks.map((link) => (
                  <Link key={link.path} to={link.path}
                    className={`block px-4 py-3 rounded-xl text-sm font-medium transition-colors ${
                      location.pathname === link.path
                        ? 'text-primary-600 bg-primary-50 dark:bg-primary-900/20'
                        : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800'
                    }`}
                  >
                    {link.name}
                  </Link>
                ))}

                <Link to="/cart" className="flex items-center justify-between px-4 py-3 rounded-xl text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">
                  <span>Cart</span>
                  {cartCount > 0 && <span className="bg-primary-100 dark:bg-primary-900/30 text-primary-600 px-2 py-0.5 rounded-full text-xs">{cartCount}</span>}
                </Link>

                <Link to="/wishlist" className="flex items-center justify-between px-4 py-3 rounded-xl text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">
                  <span>Wishlist</span>
                  {wishlistCount > 0 && <span className="bg-red-100 dark:bg-red-900/30 text-red-600 px-2 py-0.5 rounded-full text-xs">{wishlistCount}</span>}
                </Link>

                {/* Mobile — Become a Seller */}
                <button
                  onClick={() => { setIsMenuOpen(false); setIsArtisanModalOpen(true); }}
                  className="w-full text-left px-4 py-3 rounded-xl text-sm font-medium text-primary-600 dark:text-primary-400 hover:bg-primary-50 dark:hover:bg-primary-900/20"
                >
                  Become a Seller ↗
                </button>

                {isAuthenticated ? (
                  <>
                    <Link to="/profile" className="block px-4 py-3 rounded-xl text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">My Profile</Link>
                    <Link to="/orders" className="block px-4 py-3 rounded-xl text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">My Orders</Link>
                    {isAdmin() && (
                      <Link to="/admin" className="block px-4 py-3 rounded-xl text-sm font-medium text-primary-600 hover:bg-primary-50 dark:hover:bg-primary-900/20">Admin Dashboard</Link>
                    )}
                    <button onClick={handleLogout} className="w-full text-left px-4 py-3 rounded-xl text-sm font-medium text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20">
                      Logout
                    </button>
                  </>
                ) : (
                  <div className="flex gap-3 px-4 pt-4">
                    <Link to="/login" className="flex-1 text-center py-2.5 border border-gray-300 dark:border-gray-600 rounded-xl text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">Login</Link>
                    <Link to="/register" className="flex-1 btn-primary text-center">Sign Up</Link>
                  </div>
                )}
              </div>
            </div>
          </Transition>
        </div>
      </nav>

     
    </>
  );
}
