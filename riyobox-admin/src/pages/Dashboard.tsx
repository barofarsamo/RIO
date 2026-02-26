import React, { useEffect, useState } from 'react'
import { useQuery } from 'react-query'
import { 
  TrendingUp, 
  Users, 
  Film, 
  Download,
  Eye,
  Clock
} from 'lucide-react'
import { movieService } from '../services/movie'
import { useWebSocket } from '../hooks/useWebSocket'
import StatsCard from '../components/StatsCard'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'
import { Movie } from '../types'

const Dashboard: React.FC = () => {
  const [realTimeStats, setRealTimeStats] = useState({
    activeUsers: 0,
    currentViews: 0,
    recentUploads: 0,
  })

  const { data: statistics } = useQuery('dashboard-stats', async () => {
    const [movies, stats] = await Promise.all([
      movieService.getMovies(0, 5),
      movieService.getStatistics(),
    ])
    return { movies: movies.content, stats }
  })

  const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
  const wsUrl = baseURL.replace('/api', '')
  const { on } = useWebSocket(wsUrl)

  useEffect(() => {
    on('stats-update', (data: any) => {
      setRealTimeStats(data)
    })

    on('movie-added', (movie: any) => {
      console.log('New movie added:', movie)
    })

    on('user-activity', (activity: any) => {
      console.log('User activity:', activity)
    })
  }, [on])

  const statsCards = [
    {
      title: 'Total Movies',
      value: (statistics?.stats as any)?.totalMovies || 0,
      icon: <Film className="w-6 h-6" />,
      trend: '+12%',
      trendColor: 'text-green-600',
    },
    {
      title: 'Total Users',
      value: (statistics?.stats as any)?.totalUsers || 0,
      icon: <Users className="w-6 h-6" />,
      trend: '+8%',
      trendColor: 'text-green-600',
    },
    {
      title: 'Total Views',
      value: (statistics?.stats as any)?.totalViews || 0,
      icon: <Eye className="w-6 h-6" />,
      trend: '+23%',
      trendColor: 'text-green-600',
    },
    {
      title: 'Total Downloads',
      value: (statistics?.stats as any)?.totalDownloads || 0,
      icon: <Download className="w-6 h-6" />,
      trend: '+15%',
      trendColor: 'text-green-600',
    },
    {
      title: 'Active Users',
      value: realTimeStats.activeUsers,
      icon: <Users className="w-6 h-6" />,
      trend: 'Live',
      trendColor: 'text-red-600',
    },
    {
      title: 'Current Views',
      value: realTimeStats.currentViews,
      icon: <Eye className="w-6 h-6" />,
      trend: 'Live',
      trendColor: 'text-red-600',
    },
  ]

  const monthlyData = [
    { month: 'Jan', views: 4000, downloads: 2400 },
    { month: 'Feb', views: 3000, downloads: 1398 },
    { month: 'Mar', views: 2000, downloads: 9800 },
    { month: 'Apr', views: 2780, downloads: 3908 },
    { month: 'May', views: 1890, downloads: 4800 },
    { month: 'Jun', views: 2390, downloads: 3800 },
    { month: 'Jul', views: 3490, downloads: 4300 },
  ]

  return (
    <div className="p-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">Welcome to Riyobox Admin Panel</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6 mb-8">
        {statsCards.map((stat, index) => (
          <StatsCard key={index} {...stat} />
        ))}
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
        {/* Views Chart */}
        <div className="bg-white rounded-xl shadow p-6">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-semibold text-gray-900">Views & Downloads</h3>
            <TrendingUp className="w-5 h-5 text-gray-500" />
          </div>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={monthlyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Line type="monotone" dataKey="views" stroke="#3b82f6" strokeWidth={2} />
                <Line type="monotone" dataKey="downloads" stroke="#10b981" strokeWidth={2} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="bg-white rounded-xl shadow p-6">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-semibold text-gray-900">Recent Activity</h3>
            <Clock className="w-5 h-5 text-gray-500" />
          </div>
          <div className="space-y-4">
            {[
              { user: 'Ahmed Hassan', action: 'watched "Hooyo Macaan"', time: '2 mins ago' },
              { user: 'Fatima Ali', action: 'downloaded "Mogadishu Nights"', time: '5 mins ago' },
              { user: 'Omar Mohamed', action: 'subscribed to Premium', time: '10 mins ago' },
              { user: 'Aisha Yusuf', action: 'added movie to favorites', time: '15 mins ago' },
              { user: 'Mohamed Ahmed', action: 'shared "Qaran Dumar"', time: '20 mins ago' },
            ].map((activity, index) => (
              <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <p className="font-medium text-gray-900">{activity.user}</p>
                  <p className="text-sm text-gray-600">{activity.action}</p>
                </div>
                <span className="text-sm text-gray-500">{activity.time}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Recent Movies */}
      <div className="bg-white rounded-xl shadow p-6">
        <div className="flex items-center justify-between mb-6">
          <h3 className="text-lg font-semibold text-gray-900">Recent Movies</h3>
          <Film className="w-5 h-5 text-gray-500" />
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Movie</th>
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Category</th>
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Views</th>
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Downloads</th>
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Rating</th>
                <th className="text-left py-3 px-4 text-gray-600 font-medium">Added</th>
              </tr>
            </thead>
            <tbody>
              {statistics?.movies?.map((movie: Movie) => (
                <tr key={movie.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4">
                    <div className="flex items-center space-x-3">
                      <img
                        src={movie.thumbnailUrl}
                        alt={movie.title}
                        className="w-12 h-16 object-cover rounded"
                      />
                      <div>
                        <p className="font-medium text-gray-900">{movie.title}</p>
                        <p className="text-sm text-gray-600">{movie.releaseYear}</p>
                      </div>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    <div className="flex flex-wrap gap-1">
                      {movie.categories.slice(0, 2).map((cat: string) => (
                        <span key={cat} className="px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded">
                          {cat}
                        </span>
                      ))}
                    </div>
                  </td>
                  <td className="py-3 px-4">{movie.views.toLocaleString()}</td>
                  <td className="py-3 px-4">{movie.downloads.toLocaleString()}</td>
                  <td className="py-3 px-4">
                    <div className="flex items-center">
                      <span className="text-yellow-500">★</span>
                      <span className="ml-1">{movie.rating}</span>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    {new Date(movie.createdAt).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
