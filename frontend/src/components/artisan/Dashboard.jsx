const Dashboard = () => {
  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Dashboard</h2>

      <div className="grid grid-cols-3 gap-6">
        <div className="bg-white p-6 shadow rounded">
          Total Products
        </div>
        <div className="bg-white p-6 shadow rounded">
          Active Products
        </div>
        <div className="bg-white p-6 shadow rounded">
          Pending Requests
        </div>
      </div>
    </div>
  );
};

export default Dashboard;