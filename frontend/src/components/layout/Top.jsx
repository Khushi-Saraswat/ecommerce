
import { useNavigate } from 'react-router-dom';
import { artisanAPI } from '../../services/api'; // ← adjust path to your actual api file

   // ─── Social Icons ────────────────────────────────────────────────────────────

const FacebookIcon = () => (
  <svg viewBox="0 0 24 24" fill="currentColor" className="w-3.5 h-3.5">
    <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z" />
  </svg>
);

const InstagramIcon = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-3.5 h-3.5">
    <rect x="2" y="2" width="20" height="20" rx="5" ry="5" />
    <path d="M16 11.37A4 4 0 1112.63 8 4 4 0 0116 11.37z" />
    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5" />
  </svg>
);

const TwitterIcon = () => (
  <svg viewBox="0 0 24 24" fill="currentColor" className="w-3.5 h-3.5">
    <path d="M22 4s-.7 2.1-2 3.4c1.6 10-9.4 17.3-18 11.6 2.2.1 4.4-.6 6-2C3 15.5.5 9.6 3 5c2.2 2.6 5.6 4.1 9 4-.9-4.2 4-6.6 7-3.8 1.1 0 3-1.2 3-1.2z" />
  </svg>
);

const YoutubeIcon = () => (
  <svg viewBox="0 0 24 24" fill="currentColor" className="w-3.5 h-3.5">
    <path d="M22.54 6.42a2.78 2.78 0 00-1.95-1.96C18.88 4 12 4 12 4s-6.88 0-8.59.46a2.78 2.78 0 00-1.95 1.96A29 29 0 001 12a29 29 0 00.46 5.58a2.78 2.78 0 001.95 1.96C5.12 20 12 20 12 20s6.88 0 8.59-.46a2.78 2.78 0 001.95-1.96A29 29 0 0023 12a29 29 0 00-.46-5.58z" />
    <polygon points="9.75,15.02 15.5,12 9.75,8.98 9.75,15.02" fill="white" />
  </svg>
);

const PinterestIcon = () => (
  <svg viewBox="0 0 24 24" fill="currentColor" className="w-3.5 h-3.5">
    <path d="M12 0C5.373 0 0 5.373 0 12c0 5.084 3.163 9.426 7.627 11.174-.105-.949-.2-2.405.042-3.441.218-.937 1.407-5.965 1.407-5.965s-.359-.719-.359-1.782c0-1.668.967-2.914 2.171-2.914 1.023 0 1.518.769 1.518 1.69 0 1.029-.655 2.568-.994 3.995-.283 1.194.599 2.169 1.777 2.169 2.133 0 3.772-2.249 3.772-5.495 0-2.873-2.064-4.882-5.012-4.882-3.414 0-5.418 2.561-5.418 5.207 0 1.031.397 2.138.893 2.738a.36.36 0 01.083.345l-.333 1.36c-.053.22-.174.267-.402.161-1.499-.698-2.436-2.889-2.436-4.649 0-3.785 2.75-7.262 7.929-7.262 4.163 0 7.398 2.967 7.398 6.931 0 4.136-2.607 7.464-6.227 7.464-1.216 0-2.359-.632-2.75-1.378l-.748 2.853c-.271 1.043-1.002 2.35-1.492 3.146C9.57 23.812 10.763 24 12 24c6.627 0 12-5.373 12-12S18.627 0 12 0z" />
  </svg>
);

export default function Top(){

const navigate = useNavigate();

const socialLinks = [
  { icon: <FacebookIcon />, href: "#", label: "Facebook", hoverClass: "hover:text-blue-500" },
  { icon: <InstagramIcon />, href: "#", label: "Instagram", hoverClass: "hover:text-pink-500" },
  { icon: <TwitterIcon />, href: "#", label: "Twitter", hoverClass: "hover:text-sky-400" },
  { icon: <YoutubeIcon />, href: "#", label: "YouTube", hoverClass: "hover:text-red-500" },
  { icon: <PinterestIcon />, href: "#", label: "Pinterest", hoverClass: "hover:text-red-400" },
];


  const handleBecomeSeller = async () => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  if (!token) {
    console.log("hi..... loginn");
    navigate("/login");
    return;
  }

  if (role !== "ARTISAN") {
    console.log("nnnn");
    alert("You are not registered as artisan");
    return;
  }

  try {
    const response = await artisanAPI.getProfileExists();
   
    console.log("response data",response.data);
    
    if (!response.data.setProfileExists) {
      navigate("/apply-artisan");
    } 
    else{
       const response = await artisanAPI.getKYCStatus();

      console.log("KYC RESPONSE:", response.data);

      if (response.data.kycStatus === "PENDING") {
        navigate("/waiting-approval");
       }

     if (response.data.kycStatus === "APPROVED") {
      navigate("/artisan/dashboard");
    }
    }
  } catch (err) {
    console.error("Failed to fetch artisan profile:", err);
  }
};
   

  return (
    <div className="w-full bg-gray-900 dark:bg-gray-950 border-b border-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-2 flex items-center justify-between flex-wrap gap-2">
        {/* Left — tagline */}
        <p className="text-[11px] text-gray-400 tracking-widest uppercase hidden sm:block">
          ✦ Handcrafted goods — Direct from artisans
        </p>

        {/* Right — social + CTAs */}
        <div className="flex items-center gap-4 ml-auto">
          {/* Social Icons */}
          <div className="flex items-center gap-2.5">
            {socialLinks.map((s) => (
              <a
                key={s.label}
                href={s.href}
                aria-label={s.label}
                className={`text-gray-500 transition-colors duration-200 ${s.hoverClass}`}
              >
                {s.icon}
              </a>
            ))}
          </div>

          <span className="w-px h-4 bg-gray-700" />

          {/* Contact Us */}
          <a
            href="/contact"
            className="text-[11px] font-medium text-gray-400 hover:text-white tracking-wide uppercase transition-colors duration-200"
          >
            Contact Us
          </a>

          <span className="w-px h-4 bg-gray-700" />

          {/* Become a Seller */}
          <button
            onClick={handleBecomeSeller}
            className="text-[11px] font-semibold tracking-wide uppercase text-primary-400 
                       hover:text-primary-300 transition-colors duration-200"
          >
            Become a Seller ↗
          </button>
        </div>
      </div>
    </div>
  );
}
