export default function Skeleton({ className = '', variant = 'text', count = 1 }) {
  const baseClasses = 'skeleton animate-shimmer';
  
  const variants = {
    text: 'h-4 w-full rounded',
    title: 'h-6 w-3/4 rounded',
    avatar: 'h-12 w-12 rounded-full',
    thumbnail: 'h-24 w-24 rounded-lg',
    image: 'aspect-square w-full rounded-xl',
    card: 'h-48 w-full rounded-xl',
    button: 'h-10 w-24 rounded-lg',
    circle: 'rounded-full',
    rectangle: 'rounded-lg',
  };

  const skeletons = Array(count).fill(null);

  return (
    <>
      {skeletons.map((_, index) => (
        <div
          key={index}
          className={`${baseClasses} ${variants[variant]} ${className}`}
        />
      ))}
    </>
  );
}

export function ProductCardSkeleton() {
  return (
    <div className="card overflow-hidden animate-pulse">
      <Skeleton variant="image" />
      <div className="p-4 space-y-3">
        <Skeleton variant="text" className="w-1/3" />
        <Skeleton variant="title" />
        <div className="flex gap-1">
          <Skeleton className="h-4 w-4" count={5} />
        </div>
        <div className="flex items-center gap-2">
          <Skeleton variant="text" className="w-20" />
          <Skeleton variant="text" className="w-16" />
        </div>
      </div>
    </div>
  );
}

export function TableRowSkeleton({ columns = 5 }) {
  return (
    <tr className="animate-pulse">
      {Array(columns).fill(null).map((_, i) => (
        <td key={i} className="px-6 py-4">
          <Skeleton variant="text" className={i === 0 ? 'w-32' : 'w-20'} />
        </td>
      ))}
    </tr>
  );
}

export function StatCardSkeleton() {
  return (
    <div className="card animate-pulse">
      <div className="flex items-center justify-between mb-4">
        <Skeleton className="h-12 w-12 rounded-xl" />
        <Skeleton className="h-4 w-12" />
      </div>
      <Skeleton variant="title" className="mb-2" />
      <Skeleton variant="text" className="w-24" />
    </div>
  );
}
