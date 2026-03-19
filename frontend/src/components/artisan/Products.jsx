import { useEffect, useState } from "react";
import api from "../../api/axios";

const Products = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.get("/artisans/1/product") // replace 1 with real artisanId
      .then(res => setProducts(res.data.content))
      .catch(err => console.log(err));
  }, []);

  return (
    <table className="w-full bg-white shadow">
      <thead className="bg-gray-200">
        <tr>
          <th>Name</th>
          <th>Price</th>
          <th>Stock</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {products.map(p => (
          <tr key={p.id} className="text-center border-t">
            <td>{p.name}</td>
            <td>{p.price}</td>
            <td>{p.stock}</td>
            <td>{p.active ? "Active" : "Inactive"}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Products;