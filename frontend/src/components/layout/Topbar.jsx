const Topbar = () => {
  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <div className="bg-white shadow p-4 flex justify-between">
      <span className="font-semibold">
        Welcome, {user?.username}
      </span>
      <span className="bg-yellow-200 px-3 py-1 rounded text-sm">
        KYC: {user?.kycStatus || "PENDING"}
      </span>
    </div>
  );
};

export default Topbar;