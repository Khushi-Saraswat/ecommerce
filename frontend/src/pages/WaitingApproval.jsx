import { useNavigate } from 'react-router-dom';

export default function WaitingApproval() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-950 px-4">
      <div className="w-full max-w-md text-center">
        
        {/* Icon */}
        <div className="w-20 h-20 mx-auto mb-6 rounded-2xl bg-yellow-100 dark:bg-yellow-900/30 
                        flex items-center justify-center">
          <span className="text-4xl">⏳</span>
        </div>

        {/* Heading */}
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-3">
          Application Under Review
        </h1>
        <p className="text-gray-500 dark:text-gray-400 text-sm leading-relaxed mb-8">
          Your artisan profile has been submitted successfully. Our team is currently 
          reviewing your application. This usually takes <span className="font-semibold text-gray-700 dark:text-gray-300">1–2 business days</span>.
          You'll be notified once approved.
        </p>

        {/* Status Badge */}
        <div className="inline-flex items-center gap-2 px-4 py-2 bg-yellow-50 dark:bg-yellow-900/20 
                        border border-yellow-200 dark:border-yellow-800 rounded-full mb-8">
          <span className="w-2 h-2 rounded-full bg-yellow-400 animate-pulse" />
          <span className="text-xs font-semibold text-yellow-700 dark:text-yellow-400 uppercase tracking-wider">
            KYC Status: Pending
          </span>
        </div>

        {/* What's next */}
        <div className="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 
                        dark:border-gray-800 p-6 text-left mb-6 space-y-4">
          <h3 className="text-sm font-bold text-gray-700 dark:text-gray-300 uppercase tracking-wider">
            What happens next?
          </h3>
          {[
            { step: '1', text: 'Our team reviews your profile and documents' },
            { step: '2', text: 'You receive an email with the decision' },
            { step: '3', text: 'Once approved, your seller dashboard is activated' },
          ].map((item) => (
            <div key={item.step} className="flex items-start gap-3">
              <span className="w-6 h-6 rounded-full bg-primary-100 dark:bg-primary-900/30 
                               text-primary-600 dark:text-primary-400 text-xs font-bold 
                               flex items-center justify-center flex-shrink-0 mt-0.5">
                {item.step}
              </span>
              <p className="text-sm text-gray-600 dark:text-gray-400">{item.text}</p>
            </div>
          ))}
        </div>

        {/* Actions */}
        <div className="flex gap-3">
          <button
            onClick={() => navigate('/')}
            className="flex-1 py-2.5 border border-gray-200 dark:border-gray-700 rounded-xl 
                       text-sm font-medium text-gray-600 dark:text-gray-300 
                       hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            Go to Home
          </button>
          <button
            onClick={() => navigate('/contact')}
            className="flex-1 py-2.5 rounded-xl text-sm font-semibold text-white
                       bg-primary-600 hover:bg-primary-700 transition-colors"
          >
            Contact Support
          </button>
        </div>

      </div>
    </div>
  );
}