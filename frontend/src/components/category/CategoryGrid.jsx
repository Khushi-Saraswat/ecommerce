import { Link } from 'react-router-dom';

export default function CategoryGrid({ categories }) {
  const categoryImages = {
    electronics: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400',
    fashion: 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=400',
    home: 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400',
    sports: 'https://images.unsplash.com/photo-1461896836934- voices?w=400',
    beauty: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400',
    books: 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=400',
  };

  return (
    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
      {categories.map((category) => (
        <Link
          key={category.id}
          to={`/products?category=${category.id}`}
          className="group"
        >
          <div className="relative aspect-square rounded-2xl overflow-hidden bg-gray-100 dark:bg-gray-700">
            <img
              src={category.image || categoryImages[category.slug] || 'https://via.placeholder.com/200'}
              alt={category.name}
              className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
            />
            <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/20 to-transparent" />
            <div className="absolute bottom-0 left-0 right-0 p-4">
              <h3 className="text-white font-semibold text-center">
                {category.name}
              </h3>
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
}
