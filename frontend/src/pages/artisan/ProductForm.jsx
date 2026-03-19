import { useEffect, useRef, useState } from "react";
import { userAPI } from "../../services/api";
import { apiMultipart, BASE } from "./api"; // make sure api is imported
import { Field, Inp, SectionCard, Tex } from "./ui";

export default function ProductForm({ editProduct, onSaved, showToast }) {
  const isEdit = !!editProduct;
  const fileRef = useRef();

  const [loading, setLoading] = useState(false);
  const [previews, setPreviews] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({
    name:        editProduct?.name        || "",
    description: editProduct?.description || "",
    price:       editProduct?.price       || "",
    mrp:         editProduct?.mrp         || "",
    isActive:    editProduct?.isActive    ?? true,
    categoryId:  editProduct?.categoryId  || "",
    stock:       editProduct?.stock       || "",
  });

  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));
  const setCheck = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.checked }));

  // Fetch categories on mount
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res= await userAPI.getAllCategories();
        console.log(res.data+""+"res category data");
        setCategories(res.data || []);
      } catch (err) {
        console.error("Failed to fetch categories:", err);
        showToast("Failed to load categories", "error");
      }
    };
    fetchCategories();
  }, []);

  const handleFiles = (e) =>
    setPreviews(Array.from(e.target.files).map((f) => URL.createObjectURL(f)));

  const submit = async (e) => {
    e.preventDefault();
    const files = fileRef.current?.files;
    if (!isEdit && (!files || files.length === 0)) {
      showToast("Please select at least one image.", "error");
      return;
    }
    setLoading(true);
    try {
      const payload = {
        name:        form.name,
        description: form.description,
        price:       parseFloat(form.price)    || 0,
        mrp:         parseFloat(form.mrp)      || 0,
        isActive:    form.isActive,
        categoryId:  parseInt(form.categoryId) || null,
        stock:       parseInt(form.stock)      || 0,
      };
      const fd = new FormData();
      fd.append("product", JSON.stringify(payload));
      if (files)
        Array.from(files).forEach((f) =>
          fd.append(isEdit ? "images" : "file", f)
        );

      const url = isEdit
        ? `${BASE}/UpdateProduct?ProductId=${editProduct.id}`
        : `${BASE}/saveProduct`;

      await apiMultipart("POST", url, fd);
      showToast(isEdit ? "Product updated!" : "Product saved!", "success");
      onSaved();
    } catch (err) {
      showToast(err.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl space-y-6 mx-auto w-100">
      <div>
        <h2 className="text-xl font-bold text-gray-900 dark:text-white text-center">
          {isEdit ? `Edit "${editProduct.name}"` : "Add New Product"}
        </h2>
      </div>

      <form onSubmit={submit}>
        <SectionCard className="p-6 space-y-5">
          {/* Name */}
          <Field label="Product Name" required>
            <Inp
              value={form.name}
              onChange={set("name")}
              required
              placeholder="e.g. Hand-painted Madhubani Plate"
            />
          </Field>

          {/* Description */}
          <Field label="Description">
            <Tex
              value={form.description}
              onChange={set("description")}
              rows={3}
              placeholder="Describe materials, dimensions, technique..."
            />
          </Field>

          {/* Price + MRP */}
          <div className="grid grid-cols-2 gap-4">
            <Field label="Selling Price ₹ (price)" required>
              <Inp
                type="number" min="0" step="0.01"
                value={form.price} onChange={set("price")}
                required placeholder="499.00"
              />
            </Field>
            <Field label="MRP ₹ (mrp)" hint="Crossed-out original price">
              <Inp
                type="number" min="0" step="0.01"
                value={form.mrp} onChange={set("mrp")}
                placeholder="699.00"
              />
            </Field>
          </div>

          {/* Stock + Category Dropdown */}
          <div className="grid grid-cols-2 gap-4">
            <Field label="Stock (Integer)">
              <Inp type="number" min="0" value={form.stock} onChange={set("stock")} placeholder="10" />
            </Field>

            <Field label="Category" required>
              <select
                value={form.categoryId}
                onChange={set("categoryId")}
                className="w-full border border-gray-300 dark:border-gray-700 rounded-xl p-2"
                required
              >
                <option value="">Select category</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
            </Field>
          </div>

          {/* isActive */}
          <div className="flex items-center gap-3 p-4 bg-gray-50 dark:bg-gray-700/40 rounded-xl border border-gray-100 dark:border-gray-700">
            <input
              id="isActive" type="checkbox"
              checked={form.isActive} onChange={setCheck("isActive")}
              className="w-4 h-4 accent-primary-600 cursor-pointer"
            />
            <label htmlFor="isActive" className="text-sm font-medium text-gray-700 dark:text-gray-300 cursor-pointer select-none">
              <span className="font-semibold">isActive</span>
              <span className="text-gray-400 dark:text-gray-500 font-normal ml-2">— listing visible on storefront</span>
            </label>
          </div>

          {/* Images */}
          <Field label={isEdit ? "Replace Images (optional)" : "Product Images"} required={!isEdit}>
            <div
              onClick={() => fileRef.current?.click()}
              className="border-2 border-dashed border-gray-200 dark:border-gray-700 rounded-xl p-6 text-center cursor-pointer hover:border-primary-400 dark:hover:border-primary-500 transition-colors"
            >
              <input ref={fileRef} type="file" multiple accept="image/*" onChange={handleFiles} className="hidden" />
              {previews.length > 0 ? (
                <div className="flex flex-wrap gap-2 justify-center">
                  {previews.map((src, i) => (
                    <img
                      key={i} src={src} alt=""
                      className="w-20 h-20 object-cover rounded-lg border border-gray-200 dark:border-gray-700"
                    />
                  ))}
                  <div className="w-20 h-20 border-2 border-dashed border-gray-200 dark:border-gray-600 rounded-lg flex items-center justify-center text-gray-400 text-xl">
                    +
                  </div>
                </div>
              ) : (
                <>
                  <p className="text-3xl mb-2">🖼️</p>
                  <p className="text-sm text-gray-500 dark:text-gray-400">Click to select images</p>
                  <p className="text-xs text-gray-400 mt-1">Sent as multipart · key: {isEdit ? "images" : "file"}</p>
                </>
              )}
            </div>
          </Field>

          {/* Buttons */}
          <div className="flex gap-3 pt-1">
            <button
              type="button" onClick={onSaved}
              className="flex-1 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl text-sm font-medium text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit" disabled={loading}
              className="flex-1 btn-primary py-2.5 text-sm disabled:opacity-60 flex items-center justify-center gap-2"
            >
              {loading && (
                <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              )}
              {isEdit ? "Update Product" : "Save Product"}
            </button>
          </div>
        </SectionCard>
      </form>
    </div>
  );
}