import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { Link } from 'react-router-dom'
import { 
  Search, 
  Filter, 
  MoreVertical,
  Edit,
  Trash2,
  Eye,
  Download,
  Star
} from 'lucide-react'
import { movieService } from '../../services/movie'
import toast from 'react-hot-toast'
import { Movie } from '../../types'

const MovieList: React.FC = () => {
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [categoryFilter, setCategoryFilter] = useState('all')
  
  const queryClient = useQueryClient()
  
  const { data, isLoading } = useQuery(
    ['movies', page, search, categoryFilter],
    () => movieService.getMovies(page, 10)
  )
  
  const deleteMutation = useMutation(
    (id: string) => movieService.deleteMovie(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['movies'])
        toast.success('Movie deleted successfully')
      },
      onError: () => {
        toast.error('Failed to delete movie')
      }
    }
  )
  
  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this movie?')) {
      deleteMutation.mutate(id)
    }
  }
  
  const categories = [
    'All', 'Action', 'Comedy', 'Drama', 'Horror', 'Romance',
    'Somali Originals', 'Documentary', 'New Releases'
  ]
  
  return (
    <div className="p-8">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Movies</h1>
          <p className="text-gray-600">Manage your movie library</p>
        </div>
        <Link
          to="/movies/create"
          className="mt-4 sm:mt-0 inline-flex items-center px-4 py-2 bg-red-600 text-white font-medium rounded-lg hover:bg-red-700 transition-colors"
        >
          <span className="mr-2">+</span>
          Add New Movie
        </Link>
      </div>
      
      {/* Filters */}
      <div className="bg-white rounded-xl shadow p-6 mb-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search movies..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
            />
          </div>
          
          {/* Category Filter */}
          <div className="relative">
            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
            <select
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent appearance-none"
            >
              {categories.map((cat) => (
                <option key={cat} value={cat.toLowerCase()}>
                  {cat}
                </option>
              ))}
            </select>
          </div>
          
          {/* Status Filter */}
          <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent">
            <option value="all">All Status</option>
            <option value="featured">Featured Only</option>
            <option value="somali">Somali Originals</option>
          </select>
        </div>
      </div>
      
      {/* Movies Table */}
      <div className="bg-white rounded-xl shadow overflow-hidden">
        {isLoading ? (
          <div className="p-8 text-center">
            <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-red-600"></div>
          </div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Movie</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Category</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Views</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Downloads</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Rating</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Status</th>
                    <th className="py-4 px-6 text-left text-sm font-semibold text-gray-900">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {data?.content.map((movie: Movie) => (
                    <tr key={movie.id} className="hover:bg-gray-50">
                      <td className="py-4 px-6">
                        <div className="flex items-center space-x-4">
                          <img
                            src={movie.thumbnailUrl}
                            alt={movie.title}
                            className="w-16 h-24 object-cover rounded-lg"
                          />
                          <div>
                            <h4 className="font-medium text-gray-900">{movie.title}</h4>
                            <p className="text-sm text-gray-600">{movie.releaseYear} • {movie.duration} min</p>
                          </div>
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex flex-wrap gap-1 max-w-xs">
                          {movie.categories.slice(0, 2).map((cat: string) => (
                            <span
                              key={cat}
                              className="inline-block px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded"
                            >
                              {cat}
                            </span>
                          ))}
                          {movie.categories.length > 2 && (
                            <span className="text-xs text-gray-500">
                              +{movie.categories.length - 2}
                            </span>
                          )}
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex items-center">
                          <Eye className="w-4 h-4 text-gray-500 mr-2" />
                          {movie.views.toLocaleString()}
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex items-center">
                          <Download className="w-4 h-4 text-gray-500 mr-2" />
                          {movie.downloads.toLocaleString()}
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex items-center">
                          <Star className="w-4 h-4 text-yellow-500 mr-1" />
                          <span className="font-medium">{movie.rating}</span>
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex space-x-2">
                          {movie.isFeatured && (
                            <span className="px-2 py-1 bg-red-100 text-red-700 text-xs font-medium rounded">
                              Featured
                            </span>
                          )}
                          {movie.isSomaliOriginal && (
                            <span className="px-2 py-1 bg-blue-100 text-blue-700 text-xs font-medium rounded">
                              Somali
                            </span>
                          )}
                        </div>
                      </td>
                      <td className="py-4 px-6">
                        <div className="flex items-center space-x-3">
                          <Link
                            to={`/movies/${movie.id}/edit`}
                            className="p-1 text-gray-600 hover:text-red-600"
                          >
                            <Edit className="w-4 h-4" />
                          </Link>
                          <button
                            onClick={() => handleDelete(movie.id)}
                            className="p-1 text-gray-600 hover:text-red-600"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                          <button className="p-1 text-gray-600 hover:text-gray-900">
                            <MoreVertical className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            
            {/* Pagination */}
            {data && data.totalPages > 1 && (
              <div className="px-6 py-4 border-t border-gray-200">
                <div className="flex items-center justify-between">
                  <div className="text-sm text-gray-700">
                    Showing <span className="font-medium">{(page * 10) + 1}</span> to{' '}
                    <span className="font-medium">{Math.min((page + 1) * 10, data.totalElements)}</span> of{' '}
                    <span className="font-medium">{data.totalElements}</span> movies
                  </div>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => setPage(p => Math.max(0, p - 1))}
                      disabled={page === 0}
                      className="px-3 py-1 border border-gray-300 rounded disabled:opacity-50"
                    >
                      Previous
                    </button>
                    {Array.from({ length: Math.min(5, data.totalPages) }, (_, i) => {
                      let pageNum
                      if (data.totalPages <= 5) {
                        pageNum = i
                      } else if (page <= 2) {
                        pageNum = i
                      } else if (page >= data.totalPages - 3) {
                        pageNum = data.totalPages - 5 + i
                      } else {
                        pageNum = page - 2 + i
                      }
                      
                      return (
                        <button
                          key={pageNum}
                          onClick={() => setPage(pageNum)}
                          className={`px-3 py-1 rounded ${
                            page === pageNum
                              ? 'bg-red-600 text-white'
                              : 'border border-gray-300'
                          }`}
                        >
                          {pageNum + 1}
                        </button>
                      )
                    })}
                    <button
                      onClick={() => setPage(p => Math.min(data.totalPages - 1, p + 1))}
                      disabled={page === data.totalPages - 1}
                      className="px-3 py-1 border border-gray-300 rounded disabled:opacity-50"
                    >
                      Next
                    </button>
                  </div>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}

export default MovieList
