const BASE = "/api/artisans";

const token = () => localStorage.getItem("token") || "";
const authHeaders = () => ({ Authorization: `Bearer ${token()}` });

export async function api(method, path, body) {
  const opts = {
    method,
    headers: { ...authHeaders(), "Content-Type": "application/json" },
  };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(`${BASE}${path}`, opts);
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

export async function apiMultipart(method, url, formData) {
  const res = await fetch(url, { method, headers: authHeaders(), body: formData });
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

export { BASE };
