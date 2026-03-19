// ProfileSection.jsx — copy to src/pages/artisan/ProfileSection.jsx
import { useState } from "react";
import { api } from "./api";
import { Divider, Field, Inp, SectionCard, Sel, Tex } from "./ui";

const ARTISAN_TYPES = [
  "Pottery & Ceramics", "Textile Weaving", "Wood Carving", "Metal Craft",
  "Jewelry Making", "Block Printing", "Leather Craft", "Bamboo Craft",
  "Stone Carving", "Glass Blowing", "Embroidery", "Painting",
];
const KYC_STATUSES = ["UNVERIFIED", "PENDING", "VERIFIED", "REJECTED"];

export default function ProfileSection({ existing, onSaved, showToast }) {
  const [form, setForm] = useState({
    name:         existing?.name         || "",
    username:     existing?.username     || "",
    brandName:    existing?.brandName    || "",
    artisianType: existing?.artisianType || "",
    bio:          existing?.bio          || "",
    city:         existing?.city         || "",
    state:        existing?.state        || "",
    pincode:      existing?.pincode      || "",
    kycStatus:    existing?.kycStatus    || "UNVERIFIED",
  });
  const [loading, setLoading] = useState(false);
  const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api("POST", "/updateArtisan", form);
      showToast("Profile updated successfully!", "success");
      onSaved();
    } catch (err) {
      showToast(err.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl space-y-6">
      <div>
        <h2 className="text-xl font-bold text-gray-900 dark:text-white">Update Profile</h2>
        <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
          Changes map to <code className="text-primary-500 text-xs">ArtisanRequestDTO</code>
        </p>
      </div>

      <form onSubmit={submit}>
        <SectionCard className="p-6 space-y-5">

          <p className="text-xs font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest">Personal Info</p>

          <div className="grid grid-cols-2 gap-4">
            <Field label="Full Name" required>
              <Inp name="name" value={form.name} onChange={set("name")} required placeholder="Your full name" />
            </Field>
            <Field label="Username" required>
              <Inp name="username" value={form.username} onChange={set("username")} required placeholder="@handle" />
            </Field>
          </div>

          <Divider label="Store Details" />

          <Field label="Brand Name" required>
            <Inp name="brandName" value={form.brandName} onChange={set("brandName")} required placeholder="e.g. Meera's Handloom Studio" />
          </Field>

          <div className="grid grid-cols-2 gap-4">
            <Field label="Artisan Type" required>
              <Sel name="artisianType" value={form.artisianType} onChange={set("artisianType")} required>
                <option value="">Select craft type...</option>
                {ARTISAN_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
              </Sel>
            </Field>
            <Field label="KYC Status">
              <Sel name="kycStatus" value={form.kycStatus} onChange={set("kycStatus")}>
                {KYC_STATUSES.map(s => <option key={s} value={s}>{s}</option>)}
              </Sel>
            </Field>
          </div>

          <Field label="Bio" required hint={`${form.bio.length} / 500`}>
            <Tex name="bio" value={form.bio} onChange={set("bio")} required rows={4} maxLength={500}
              placeholder="Tell buyers about your craft heritage, techniques, and passion..." />
          </Field>

          <Divider label="Location" />

          <div className="grid grid-cols-3 gap-3">
            <Field label="City" required>
              <Inp name="city" value={form.city} onChange={set("city")} required placeholder="City" />
            </Field>
            <Field label="State" required>
              <Inp name="state" value={form.state} onChange={set("state")} required placeholder="State" />
            </Field>
            <Field label="Pincode" required>
              <Inp name="pincode" value={form.pincode} onChange={set("pincode")} required placeholder="6-digit" maxLength={6} pattern="[0-9]{6}" />
            </Field>
          </div>

          <button type="submit" disabled={loading}
            className="w-full btn-primary py-3 text-sm font-semibold disabled:opacity-60 flex items-center justify-center gap-2">
            {loading && <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />}
            Save Changes
          </button>

        </SectionCard>
      </form>
    </div>
  );
}
