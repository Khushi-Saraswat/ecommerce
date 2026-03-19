import { useState } from "react";


const AddProduct = () => {
  const [product, setProduct] = useState({
    name: "",
    description: "",
    price: "",
    stock: "",
    categoryId: ""
  });

  const [images, setImages] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("product", JSON.stringify(product));

    for (let i = 0; i < images.length; i++) {
      formData.append("images", images[i]);
    }

    await api.post("/artisans/saveProduct", formData);
    alert("Product Added");
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-lg">
      <input placeholder="Name"
        className="border p-2 w-full"
        onChange={e => setProduct({...product, name: e.target.value})}
      />

      <input placeholder="Price"
        className="border p-2 w-full"
        onChange={e => setProduct({...product, price: e.target.value})}
      />

      <input type="file" multiple
        onChange={e => setImages(e.target.files)}
      />

      <button className="bg-green-600 text-white px-4 py-2 rounded">
        Save Product
      </button>
    </form>
  );
};

export default AddProduct;