export interface User {
  id: string
  email: string
  name: string
  profilePicture?: string
  subscriptionPlan: 'free' | 'premium' | 'pro'
  roles: string[]
  createdAt: string
  updatedAt: string
}

export interface Movie {
  id: string
  title: string
  description: string
  thumbnailUrl: string
  videoUrl: string
  duration: number
  releaseYear: number
  rating: number
  categories: string[]
  actors: string[]
  director: string
  views: number
  downloads: number
  isFeatured: boolean
  isSomaliOriginal: boolean
  createdAt: string
  updatedAt: string
}

export interface MovieCreate {
  title: string
  description: string
  thumbnailUrl: string
  videoUrl: string
  duration: number
  releaseYear: number
  categories: string[]
  actors: string[]
  director: string
  isFeatured: boolean
  isSomaliOriginal: boolean
}

export interface MovieUpdate extends Partial<MovieCreate> {}

export interface Category {
  id: string
  name: string
  description: string
  icon: string
  movieCount: number
  createdAt: string
  updatedAt: string
}

export interface AuthResponse {
  token: string
  user: User
  message: string
}

export interface PaginatedResponse<T> {
  data: T[]
  total: number
  page: number
  limit: number
  totalPages: number
}

export interface AnalyticsData {
  totalMovies: number
  totalUsers: number
  totalViews: number
  totalDownloads: number
  monthlyStats: {
    month: string
    views: number
    downloads: number
    newUsers: number
  }[]
}
