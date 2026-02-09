import { apiGet, apiPost, apiPut, apiDelete, PageResponse } from './api'
import { Movie, MovieCreate, MovieUpdate } from '../types'

export const movieService = {
  // Get movies with pagination
  async getMovies(
    page = 0,
    size = 20,
    sort = 'createdAt,desc'
  ): Promise<PageResponse<Movie>> {
    return apiGet('/movies', { page, size, sort })
  },

  // Get all movies (no pagination)
  async getAllMovies(): Promise<Movie[]> {
    return apiGet('/movies/all')
  },

  // Get single movie
  async getMovie(id: string): Promise<Movie> {
    return apiGet(`/movies/${id}`)
  },

  // Create movie
  async createMovie(data: MovieCreate): Promise<Movie> {
    return apiPost('/movies', data)
  },

  // Update movie
  async updateMovie(id: string, data: MovieUpdate): Promise<Movie> {
    return apiPut(`/movies/${id}`, data)
  },

  // Delete movie
  async deleteMovie(id: string): Promise<void> {
    await apiDelete(`/movies/${id}`)
  },

  // Get featured movies
  async getFeaturedMovies(): Promise<Movie[]> {
    return apiGet('/movies/featured')
  },

  // Get trending movies
  async getTrendingMovies(limit = 10): Promise<Movie[]> {
    return apiGet('/movies/trending', { limit })
  },

  // Search movies
  async searchMovies(
    query: string,
    page = 0,
    size = 20
  ): Promise<PageResponse<Movie>> {
    return apiGet('/movies/search', { q: query, page, size })
  },

  // Get movies by category
  async getMoviesByCategory(
    categoryId: string,
    page = 0,
    size = 20
  ): Promise<PageResponse<Movie>> {
    return apiGet(`/categories/${categoryId}/movies`, { page, size })
  },

  // Upload thumbnail
  async uploadThumbnail(file: File): Promise<string> {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await fetch('/api/upload/thumbnail', {
      method: 'POST',
      body: formData,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    if (!response.ok) {
      throw new Error('Upload failed')
    }
    
    const data = await response.json()
    return data.url
  },

  // Upload video (with progress)
  async uploadVideo(
    file: File,
    onProgress?: (progress: number) => void
  ): Promise<string> {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest()
      const formData = new FormData()
      
      formData.append('file', file)
      
      xhr.open('POST', '/api/upload/video')
      xhr.setRequestHeader('Authorization', `Bearer ${localStorage.getItem('token')}`)
      
      xhr.upload.onprogress = (event) => {
        if (event.lengthComputable && onProgress) {
          const progress = Math.round((event.loaded / event.total) * 100)
          onProgress(progress)
        }
      }
      
      xhr.onload = () => {
        if (xhr.status === 200) {
          try {
            const response = JSON.parse(xhr.responseText)
            resolve(response.url)
          } catch (error) {
            reject(new Error('Invalid response'))
          }
        } else {
          reject(new Error(`Upload failed: ${xhr.statusText}`))
        }
      }
      
      xhr.onerror = () => {
        reject(new Error('Network error'))
      }
      
      xhr.send(formData)
    })
  },

  // Get movie statistics
  async getStatistics(): Promise<any> {
    return apiGet('/analytics/movies')
  }
}
