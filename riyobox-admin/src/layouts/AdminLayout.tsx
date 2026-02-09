import React from 'react'
import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { 
  Home, 
  Film, 
  Folder, 
  Users, 
  BarChart3, 
  Settings, 
  LogOut,
  PlusCircle
} from 'lucide-react'

const AdminLayout: React.FC = () => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { path: '/', label: 'Dashboard', icon: <Home size={20} /> },
    { path: '/movies', label: 'Movies', icon: <Film size={20} /> },
    { path: '/categories', label: 'Categories', icon: <Folder size={20} /> },
    { path: '/users', label: 'Users', icon: <Users size={20} /> },
    { path: '/analytics', label: 'Analytics', icon: <BarChart3 size={20} /> },
    { path: '/settings', label: 'Settings', icon: <Settings size={20} /> },
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Sidebar */}
      <aside className="fixed inset-y-0 left-0 z-50 w-64 bg-white border-r border-gray-200">
        <div className="flex flex-col h-full">
          {/* Logo */}
          <div className="flex items-center h-16 px-6 border-b border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-8 h-8 bg-red-600 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold">R</span>
              </div>
              <span className="text-xl font-bold text-gray-900">Riyobox Admin</span>
            </div>
          </div>

          {/* User Profile */}
          <div className="px-6 py-4 border-b border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                <span className="text-red-600 font-bold text-lg">
                  {user?.name?.charAt(0).toUpperCase()}
                </span>
              </div>
              <div>
                <p className="font-medium text-gray-900">{user?.name}</p>
                <p className="text-sm text-gray-500">{user?.email}</p>
              </div>
            </div>
          </div>

          {/* Navigation */}
          <nav className="flex-1 px-4 py-6 space-y-2">
            {navItems.map((item) => (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                    isActive
                      ? 'bg-red-50 text-red-600'
                      : 'text-gray-700 hover:bg-gray-100'
                  }`
                }
              >
                {item.icon}
                <span className="font-medium">{item.label}</span>
              </NavLink>
            ))}
          </nav>

          {/* Quick Actions & Logout */}
          <div className="p-4 border-t border-gray-200 space-y-2">
            <button
              onClick={() => navigate('/movies/create')}
              className="flex items-center justify-center w-full space-x-2 px-4 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
            >
              <PlusCircle size={20} />
              <span className="font-medium">Add Movie</span>
            </button>
            
            <button
              onClick={handleLogout}
              className="flex items-center justify-center w-full space-x-2 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <LogOut size={20} />
              <span className="font-medium">Logout</span>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="pl-64">
        <Outlet />
      </main>
    </div>
  )
}

export default AdminLayout
