import { useState } from "react";
import api from "../../api/axios";

const Profile = () => {
  const [form, setForm] = useState({
    name: "",
    brandName: "",
    artisianType: "",
    bio: "",
    city: "",
    state: "",
    pincode: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post("/artisans/updateArtisan", form);
    alert("Profile Updated");
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-lg">
      {Object.keys(form).map((key) => (
        <input
          key={key}
          placeholder={key}
          className="border p-2 w-full"
          onChange={(e) =>
            setForm({ ...form, [key]: e.target.value })
          }
        />
      ))}

      <button className="bg-blue-600 text-white px-4 py-2 rounded">
        Save
      </button>
    </form>
  );
};

export default Profile;