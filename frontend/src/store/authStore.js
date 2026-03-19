import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { authAPI, userAPI } from '../services/api';




export const useAuthStore = create(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      error: null,

      login: async (credentials) => {
  set({ isLoading: true, error: null });

  try {
    const response = await authAPI.login(credentials);
    const data = response.data; // ← get data first

    console.log("Backend data:", data); // ← log this to verify structure

    const token = data.jwt;                    // ✅
    const role = data.refreshToken.user.role;  // ✅

    localStorage.setItem("token", token);
    localStorage.setItem("role", role);

    set({
      user: data.refreshToken.user,
      token: data.jwt,
      refreshToken: data.refreshToken.token,
      isAuthenticated: true,
      isLoading: false,
    });

    return { success: true };

  } catch (error) {
    console.error("Login error:", error); // ← add this to see real error
    set({
      error: error.response?.data?.message || 'Login failed',
      isLoading: false,
    });
    return { success: false, error: error.response?.data?.message };
  }
},

        register: async (userData) => {
        set({ isLoading: true, error: null });
        try {
        
          const response = await authAPI.register(userData);
          console.log(response.data+""+"response");
          console.log(response.data.error+""+"response error");
          const { token, user } = response.data.data;

          set({
            user,
            token,
            isAuthenticated: true,
            isLoading: false,
          });
          localStorage.removeItem("auth-storage");
          return { success: true };
        } catch (error) {
          set({
            error: error.response?.data?.message || 'Registration failed',
            isLoading: false,
          });
          return { success: false, error: error.response?.data?.message };
        }
      },

      logout: () => {
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        });
      },

      updateProfile: async (data) => {
        set({ isLoading: true, error: null });
        try {
          const response = await userAPI.updateProfile(data);
          set({
            user: response.data.data,
            isLoading: false,
          });
          return { success: true };
        } catch (error) {
          set({
            error: error.response?.data?.message || 'Update failed',
            isLoading: false,
          });
          return { success: false, error: error.response?.data?.message };
        }
      },

      fetchProfile: async () => {
        if (!get().token) return;
        try {
          const response = await userAPI.getProfile();
          set({ user: response.data.data });
        } catch (error) {
          console.error('Failed to fetch profile:', error);
        }
      },

      isAdmin: () => {
        const user = get().user;
        return user?.roles?.includes('ADMIN') || false;
      },

      isArtisan:() =>{
       return localStorage.getItem("role") == "ARTISAN";
      }



    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);
