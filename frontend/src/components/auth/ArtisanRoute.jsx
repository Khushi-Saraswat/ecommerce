import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../../store/authStore';

export default function ArtisanRoute({ children }) {
  const { isAuthenticated, isArtisan } = useAuthStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!isArtisan) {
    return <Navigate to="/" replace />;
  }

  return children;
}