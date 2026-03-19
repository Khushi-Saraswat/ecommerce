import { Link } from 'react-router-dom';
import { 
  ShoppingBagIcon, 
  MagnifyingGlassIcon, 
  FolderIcon,
  DocumentIcon,
  UserIcon,
  ExclamationCircleIcon,
  HeartIcon
} from '@heroicons/react/24/outline';

const icons = {
  cart: ShoppingBagIcon,
  search: MagnifyingGlassIcon,
  folder: FolderIcon,
  document: DocumentIcon,
  user: UserIcon,
  error: ExclamationCircleIcon,
  wishlist: HeartIcon,
  orders: ShoppingBagIcon,
};

export default function EmptyState({
  icon = 'folder',
  title = 'No items found',
  description = 'There are no items to display.',
  action,
  actionLabel,
  actionTo,
}) {
  const IconComponent = icons[icon] || icons.folder;

  return (
    <div className="flex flex-col items-center justify-center py-16 px-4 text-center animate-fade-in">
      <div className="w-24 h-24 bg-gray-100 dark:bg-gray-800 rounded-full flex items-center justify-center mb-6">
        <IconComponent className="h-12 w-12 text-gray-400 dark:text-gray-500" />
      </div>
      
      <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
        {title}
      </h3>
      
      <p className="text-gray-500 dark:text-gray-400 max-w-sm mb-8">
        {description}
      </p>

      {(action || actionTo) && (
        actionTo ? (
          <Link to={actionTo} className="btn-primary">
            {actionLabel || 'Get Started'}
          </Link>
        ) : (
          <button onClick={action} className="btn-primary">
            {actionLabel || 'Get Started'}
          </button>
        )
      )}
    </div>
  );
}
