import { Outlet } from 'react-router-dom';
import Navbar from "./components/layout/Navbar";

const HomeLayout = () => {
  return (
    <div>
      <Navbar/> {/* Home specific navbar */}
      <main>
        <Outlet /> {/* Renders child routes here */}
      </main>
      <footer>{/* Home specific footer */}</footer>
    </div>
  );
};

export default HomeLayout;
