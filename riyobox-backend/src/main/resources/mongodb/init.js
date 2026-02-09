// Initialize Riyobox Database
db = db.getSiblingDB('riyobox');

// Create admin user
db.createUser({
  user: 'riyobox_admin',
  pwd: 'admin123',
  roles: [
    { role: 'dbOwner', db: 'riyobox' },
    { role: 'readWrite', db: 'riyobox' }
  ]
});

// Create indexes for better performance
db.movies.createIndex({ title: 'text', description: 'text' }, { 
  weights: { title: 10, description: 5 },
  name: 'MovieTextIndex' 
});

db.movies.createIndex({ isFeatured: 1 });
db.movies.createIndex({ isSomaliOriginal: 1 });
db.movies.createIndex({ categories: 1 });
db.movies.createIndex({ releaseYear: -1 });
db.movies.createIndex({ rating: -1 });
db.movies.createIndex({ views: -1 });
db.movies.createIndex({ downloads: -1 });

db.users.createIndex({ email: 1 }, { unique: true });
db.users.createIndex({ subscriptionPlan: 1 });
db.users.createIndex({ createdAt: -1 });

db.categories.createIndex({ name: 1 }, { unique: true });
db.categories.createIndex({ movieCount: -1 });

db.downloads.createIndex({ userId: 1, movieId: 1 });
db.downloads.createIndex({ downloadedAt: -1 });

// Insert default categories
const categories = [
  {
    name: 'Action',
    description: 'Action-packed movies with thrilling sequences',
    icon: '🎬',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Comedy',
    description: 'Funny movies to make you laugh',
    icon: '😂',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Drama',
    description: 'Emotional and intense stories',
    icon: '🎭',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Somali Originals',
    description: 'Original Somali movies and series',
    icon: '🇸🇴',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Romance',
    description: 'Love stories and romantic movies',
    icon: '💘',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Horror',
    description: 'Scary and thrilling movies',
    icon: '👻',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: 'Documentary',
    description: 'Educational and informative documentaries',
    icon: '📽️',
    movieCount: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  }
];

db.categories.insertMany(categories);

print('✅ Database initialized successfully!');
