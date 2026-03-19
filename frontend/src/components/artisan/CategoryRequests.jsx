import { useEffect, useState } from "react";

const CategoryRequests = () => {
  const [name, setName] = useState("");
  const [requests, setRequests] = useState([]);

  const fetchRequests = async () => {
    const res = await api.get("/artisans/my");
    setRequests(res.data);
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post("/artisans/create", { name });
    setName("");
    fetchRequests();
  };

  return (
    <div>
      <form onSubmit={handleSubmit} className="mb-6">
        <input
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="New Category Name"
          className="border p-2 mr-2"
        />
        <button className="bg-blue-600 text-white px-4 py-2 rounded">
          Request
        </button>
      </form>

      <table className="w-full bg-white shadow">
        <thead className="bg-gray-200">
          <tr>
            <th>Name</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {requests.map(r => (
            <tr key={r.id} className="text-center border-t">
              <td>{r.name}</td>
              <td>{r.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CategoryRequests;