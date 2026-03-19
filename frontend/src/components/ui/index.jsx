import { useState } from 'react';
import { 
  StarIcon,
  CheckCircleIcon,
  XCircleIcon,
  ExclamationCircleIcon,
  InformationCircleIcon,
  XMarkIcon
} from '@heroicons/react/24/outline';
import { StarIcon as StarIconSolid } from '@heroicons/react/24/solid';

// Rating Component
export function Rating({ value = 0, onChange, readonly = false, size = 'md' }) {
  const [hoverValue, setHoverValue] = useState(0);
  
  const sizes = {
    sm: 'h-4 w-4',
    md: 'h-5 w-5',
    lg: 'h-6 w-6',
  };

  const handleClick = (rating) => {
    if (!readonly && onChange) {
      onChange(rating);
    }
  };

  return (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((star) => {
        const filled = (hoverValue || value) >= star;
        return (
          <button
            key={star}
            type="button"
            onClick={() => handleClick(star)}
            onMouseEnter={() => !readonly && setHoverValue(star)}
            onMouseLeave={() => !readonly && setHoverValue(0)}
            disabled={readonly}
            className={`${readonly ? 'cursor-default' : 'cursor-pointer hover:scale-110'} transition-transform`}
          >
            {filled ? (
              <StarIconSolid className={`${sizes[size]} text-yellow-400`} />
            ) : (
              <StarIcon className={`${sizes[size]} text-gray-300 dark:text-gray-600`} />
            )}
          </button>
        );
      })}
    </div>
  );
}

// Alert Component
export function Alert({ type = 'info', title, children, onClose, className = '' }) {
  const styles = {
    info: {
      container: 'bg-blue-50 dark:bg-blue-900/20 border-blue-200 dark:border-blue-800',
      icon: 'text-blue-500',
      title: 'text-blue-800 dark:text-blue-200',
      text: 'text-blue-700 dark:text-blue-300',
      Icon: InformationCircleIcon,
    },
    success: {
      container: 'bg-green-50 dark:bg-green-900/20 border-green-200 dark:border-green-800',
      icon: 'text-green-500',
      title: 'text-green-800 dark:text-green-200',
      text: 'text-green-700 dark:text-green-300',
      Icon: CheckCircleIcon,
    },
    warning: {
      container: 'bg-yellow-50 dark:bg-yellow-900/20 border-yellow-200 dark:border-yellow-800',
      icon: 'text-yellow-500',
      title: 'text-yellow-800 dark:text-yellow-200',
      text: 'text-yellow-700 dark:text-yellow-300',
      Icon: ExclamationCircleIcon,
    },
    error: {
      container: 'bg-red-50 dark:bg-red-900/20 border-red-200 dark:border-red-800',
      icon: 'text-red-500',
      title: 'text-red-800 dark:text-red-200',
      text: 'text-red-700 dark:text-red-300',
      Icon: XCircleIcon,
    },
  };

  const style = styles[type];
  const IconComponent = style.Icon;

  return (
    <div className={`flex gap-3 p-4 rounded-xl border ${style.container} ${className}`}>
      <IconComponent className={`h-5 w-5 flex-shrink-0 ${style.icon}`} />
      <div className="flex-1">
        {title && (
          <h4 className={`font-semibold mb-1 ${style.title}`}>{title}</h4>
        )}
        <div className={`text-sm ${style.text}`}>{children}</div>
      </div>
      {onClose && (
        <button
          onClick={onClose}
          className={`flex-shrink-0 ${style.icon} hover:opacity-75`}
        >
          <XMarkIcon className="h-5 w-5" />
        </button>
      )}
    </div>
  );
}

// Progress Bar Component
export function ProgressBar({ value = 0, max = 100, size = 'md', color = 'primary', showLabel = false }) {
  const percentage = Math.min(100, Math.max(0, (value / max) * 100));
  
  const sizes = {
    sm: 'h-1',
    md: 'h-2',
    lg: 'h-3',
  };
  
  const colors = {
    primary: 'bg-primary-600',
    success: 'bg-green-500',
    warning: 'bg-yellow-500',
    error: 'bg-red-500',
  };

  return (
    <div className="w-full">
      {showLabel && (
        <div className="flex justify-between text-sm mb-1">
          <span className="text-gray-600 dark:text-gray-400">Progress</span>
          <span className="font-medium">{Math.round(percentage)}%</span>
        </div>
      )}
      <div className={`w-full bg-gray-200 dark:bg-gray-700 rounded-full ${sizes[size]} overflow-hidden`}>
        <div
          className={`${sizes[size]} ${colors[color]} rounded-full transition-all duration-500 ease-out`}
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  );
}

// Tabs Component
export function Tabs({ tabs, activeTab, onChange, variant = 'underline' }) {
  const variants = {
    underline: {
      container: 'border-b border-gray-200 dark:border-gray-700',
      tab: 'pb-3 px-4 text-sm font-medium transition-colors',
      active: 'text-primary-600 border-b-2 border-primary-600',
      inactive: 'text-gray-500 hover:text-gray-700 dark:hover:text-gray-300',
    },
    pills: {
      container: 'bg-gray-100 dark:bg-gray-800 p-1 rounded-xl',
      tab: 'px-4 py-2 text-sm font-medium rounded-lg transition-all',
      active: 'bg-white dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm',
      inactive: 'text-gray-500 hover:text-gray-700 dark:hover:text-gray-300',
    },
  };

  const style = variants[variant];

  return (
    <div className={`flex gap-1 ${style.container}`}>
      {tabs.map((tab) => (
        <button
          key={tab.id}
          onClick={() => onChange(tab.id)}
          className={`${style.tab} ${activeTab === tab.id ? style.active : style.inactive}`}
        >
          {tab.label}
          {tab.count !== undefined && (
            <span className="ml-2 px-2 py-0.5 text-xs rounded-full bg-gray-200 dark:bg-gray-600">
              {tab.count}
            </span>
          )}
        </button>
      ))}
    </div>
  );
}

// Avatar Group Component
export function AvatarGroup({ avatars, max = 4, size = 'md' }) {
  const sizes = {
    sm: 'w-8 h-8 text-xs',
    md: 'w-10 h-10 text-sm',
    lg: 'w-12 h-12 text-base',
  };

  const displayed = avatars.slice(0, max);
  const remaining = avatars.length - max;

  return (
    <div className="flex -space-x-3">
      {displayed.map((avatar, index) => (
        <div
          key={index}
          className={`${sizes[size]} rounded-full ring-2 ring-white dark:ring-gray-800 overflow-hidden`}
        >
          {avatar.image ? (
            <img src={avatar.image} alt={avatar.name} className="w-full h-full object-cover" />
          ) : (
            <div className="w-full h-full bg-primary-100 dark:bg-primary-900 flex items-center justify-center text-primary-600 font-medium">
              {avatar.name?.charAt(0) || '?'}
            </div>
          )}
        </div>
      ))}
      {remaining > 0 && (
        <div className={`${sizes[size]} rounded-full ring-2 ring-white dark:ring-gray-800 bg-gray-200 dark:bg-gray-700 flex items-center justify-center font-medium text-gray-600 dark:text-gray-300`}>
          +{remaining}
        </div>
      )}
    </div>
  );
}

// Tooltip Component
export function Tooltip({ children, content, position = 'top' }) {
  const positions = {
    top: 'bottom-full left-1/2 -translate-x-1/2 mb-2',
    bottom: 'top-full left-1/2 -translate-x-1/2 mt-2',
    left: 'right-full top-1/2 -translate-y-1/2 mr-2',
    right: 'left-full top-1/2 -translate-y-1/2 ml-2',
  };

  return (
    <div className="relative group inline-block">
      {children}
      <div className={`tooltip ${positions[position]}`}>
        {content}
      </div>
    </div>
  );
}
