import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { artisanAPI } from '../services/api';

const artisanTypes = [
  "Pottery & Ceramics", "Textile Weaving", "Wood Carving", "Metal Craft",
  "Jewelry Making", "Block Printing", "Leather Craft", "Bamboo Craft",
  "Stone Carving", "Glass Blowing", "Embroidery", "Painting",
];

export default function ApplyArtisan() {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(true); // open by default on page load
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    brandName: '',
    artisianType: '',
    bio: '',
    city: '',
    state: '',
    pincode: '',
  });

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
    const res = await artisanAPI.applyAsArtisan(form);

    console.log("RESPONSE:", res);

    if (res.data.message === 'waiting for admin approval') {
      navigate('/waiting-approval');
    }

  } catch (err) {
    console.error(err);
    setError('Something went wrong. Please try again.');
  } finally {
    setLoading(false);
  }
  };

  const handleClose = () => {
    navigate('/'); // if they close modal, go back to home
  };

  if (!isModalOpen) return null;

  return (
    <div
      className="fixed inset-0 z-[999] flex items-center justify-center p-4"
      style={{ background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(6px)' }}
      onClick={(e) => e.target === e.currentTarget && handleClose()}
    >
      <div className="relative w-full max-w-2xl bg-white dark:bg-gray-900 rounded-2xl shadow-2xl overflow-hidden max-h-[90vh] overflow-y-auto">
        
        {/* Accent strip */}
        <div className="h-1 w-full bg-gradient-to-r from-primary-400 via-primary-600 to-primary-400" />

        {/* Header */}
        <div className="px-8 pt-8 pb-6 border-b border-gray-100 dark:border-gray-800">
          <button
            onClick={handleClose}
            className="absolute top-5 right-5 p-1.5 text-gray-400 hover:text-gray-700 dark:hover:text-gray-200 
                       hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors"
          >
            ✕
          </button>
          <div className="flex items-center gap-4">
            <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-primary-500 to-primary-700 
                            flex items-center justify-center shadow-lg">
              <span className="text-white text-lg">🪡</span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-gray-900 dark:text-white">Create Artisan Profile</h2>
              <p className="text-sm text-gray-500 dark:text-gray-400">Join our community of skilled craftspeople</p>
            </div>
          </div>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="px-8 py-6 space-y-5">
          
          {error && (
            <div className="p-3 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 
                            text-red-600 dark:text-red-400 text-sm rounded-xl">
              {error}
            </div>
          )}

          {/* Brand Name */}
          <div>
            <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
              Brand Name <span className="text-red-500">*</span>
            </label>
            <input
              name="brandName"
              value={form.brandName}
              onChange={handleChange}
              required
              placeholder="e.g. Meera's Handloom Studio"
              className="w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                         bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-white 
                         placeholder-gray-400 focus:outline-none focus:ring-2 
                         focus:ring-primary-500/20 focus:border-primary-500 transition-all text-sm"
            />
          </div>

          {/* Artisan Type */}
          <div>
            <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
              Craft Type <span className="text-red-500">*</span>
            </label>
            <select
              name="artisianType"
              value={form.artisianType}
              onChange={handleChange}
              required
              className="w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                         bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-white 
                         focus:outline-none focus:ring-2 focus:ring-primary-500/20 
                         focus:border-primary-500 transition-all text-sm cursor-pointer"
            >
              <option value="">Select your craft...</option>
              {artisanTypes.map((t) => <option key={t} value={t}>{t}</option>)}
            </select>
          </div>

          {/* Bio */}
          <div>
            <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
              Bio / Your Story <span className="text-red-500">*</span>
            </label>
            <textarea
              name="bio"
              value={form.bio}
              onChange={handleChange}
              required
              rows={4}
              maxLength={500}
              placeholder="Tell buyers about your craft heritage, techniques, and passion..."
              className="w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                         bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-white 
                         placeholder-gray-400 focus:outline-none focus:ring-2 
                         focus:ring-primary-500/20 focus:border-primary-500 transition-all 
                         text-sm resize-none"
            />
            <p className="text-right text-[11px] text-gray-400 mt-1">{form.bio.length}/500</p>
          </div>

          {/* Location */}
          <div>
            <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
              Location <span className="text-red-500">*</span>
            </label>
            <div className="grid grid-cols-3 gap-3">
              {[
                { name: 'city', placeholder: 'City' },
                { name: 'state', placeholder: 'State' },
                { name: 'pincode', placeholder: 'Pincode', maxLength: 6, pattern: '[0-9]{6}' },
              ].map((field) => (
                <input
                  key={field.name}
                  name={field.name}
                  value={form[field.name]}
                  onChange={handleChange}
                  required
                  placeholder={field.placeholder}
                  maxLength={field.maxLength}
                  pattern={field.pattern}
                  className="w-full px-4 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                             bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-white 
                             placeholder-gray-400 focus:outline-none focus:ring-2 
                             focus:ring-primary-500/20 focus:border-primary-500 transition-all text-sm"
                />
              ))}
            </div>
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-1">
            <button
              type="button"
              onClick={handleClose}
              className="flex-1 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                         text-sm font-medium text-gray-600 dark:text-gray-300 
                         hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2.5 rounded-xl text-sm font-semibold text-white
                         bg-primary-600 hover:bg-primary-700 disabled:opacity-60 
                         disabled:cursor-not-allowed transition-colors"
            >
              {loading ? 'Submitting...' : 'Create Profile'}
            </button>
          </div>

        </form>
      </div>
    </div>
  );
}