
import { Outlet } from 'react-router-dom';



const ArtisanLayout = () => {
  return (
    <div style={{ display: 'flex' }}>
      <ArtisanPanel /> {/* Artisan specific sidebar/navbar */}
      <main style={{ flexGrow: 1, padding: '20px' }}>
        <Outlet /> {/* Renders child routes here */}
      </main>
    </div>
  );
};

export default ArtisanLayout;
