import { create } from 'zustand';
import { cartAPI } from '../services/api';
import toast from 'react-hot-toast';

export const useCartStore = create((set, get) => ({
  cart: null,
  isLoading: false,
  error: null,

  fetchCart: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await cartAPI.get();
      set({ cart: response.data.data, isLoading: false });
    } catch (error) {
      set({ error: error.message, isLoading: false });
    }
  },

  addToCart: async (productId, quantity = 1) => {
    set({ isLoading: true });
    try {
      const response = await cartAPI.addItem({ productId, quantity });
      set({ cart: response.data.data, isLoading: false });
      toast.success('Added to cart!', {
        icon: '🛒',
        style: {
          borderRadius: '12px',
          background: '#333',
          color: '#fff',
        },
      });
      return { success: true };
    } catch (error) {
      set({ isLoading: false });
      toast.error(error.response?.data?.message || 'Failed to add to cart');
      return { success: false };
    }
  },

  updateItemQuantity: async (productId, quantity) => {
    if (quantity < 1) return;
    
    // Optimistic update
    const previousCart = get().cart;
    const updatedItems = previousCart?.items?.map(item =>
      item.productId === productId ? { ...item, quantity } : item
    );
    
    if (previousCart) {
      set({
        cart: {
          ...previousCart,
          items: updatedItems,
          totalItems: updatedItems?.reduce((sum, item) => sum + item.quantity, 0) || 0,
        },
        isLoading: true,
      });
    }

    try {
      const response = await cartAPI.updateItem(productId, quantity);
      set({ cart: response.data.data, isLoading: false });
    } catch (error) {
      // Rollback on error
      set({ cart: previousCart, isLoading: false });
      toast.error(error.response?.data?.message || 'Failed to update cart');
    }
  },

  removeFromCart: async (productId) => {
    // Optimistic update
    const previousCart = get().cart;
    const updatedItems = previousCart?.items?.filter(item => item.productId !== productId);
    
    if (previousCart) {
      set({
        cart: {
          ...previousCart,
          items: updatedItems,
          totalItems: updatedItems?.reduce((sum, item) => sum + item.quantity, 0) || 0,
        },
        isLoading: true,
      });
    }

    try {
      const response = await cartAPI.removeItem(productId);
      set({ cart: response.data.data, isLoading: false });
      toast.success('Item removed from cart', {
        icon: '🗑️',
      });
    } catch (error) {
      // Rollback on error
      set({ cart: previousCart, isLoading: false });
      toast.error(error.response?.data?.message || 'Failed to remove item');
    }
  },

  clearCart: async () => {
    set({ isLoading: true });
    try {
      await cartAPI.clear();
      set({ cart: null, isLoading: false });
      toast.success('Cart cleared');
    } catch (error) {
      set({ isLoading: false });
      toast.error('Failed to clear cart');
    }
  },

  applyCoupon: async (code) => {
    set({ isLoading: true });
    try {
      const response = await cartAPI.applyCoupon(code);
      set({ cart: response.data.data, isLoading: false });
      toast.success('Coupon applied successfully!', { icon: '🎉' });
      return { success: true };
    } catch (error) {
      set({ isLoading: false });
      toast.error(error.response?.data?.message || 'Invalid coupon code');
      return { success: false, error: error.response?.data?.message };
    }
  },

  removeCoupon: async () => {
    set({ isLoading: true });
    try {
      const response = await cartAPI.removeCoupon();
      set({ cart: response.data.data, isLoading: false });
      toast.success('Coupon removed');
    } catch (error) {
      set({ isLoading: false });
    }
  },

  getItemCount: () => {
    const cart = get().cart;
    return cart?.totalItems || 0;
  },

  getSubtotal: () => {
    const cart = get().cart;
    return cart?.totalPrice || 0;
  },

  getDiscount: () => {
    const cart = get().cart;
    return cart?.discount || 0;
  },
}));
