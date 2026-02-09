import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './hooks/useAuth'
import AdminLayout from './layouts/AdminLayout'
import AuthLayout from './layouts/AuthLayout'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import MovieList from './pages/Movies/MovieList'
import MovieCreate from './pages/Movies/MovieCreate'
import MovieEdit from './pages/Movies/MovieEdit'
import CategoryList from './pages/Categories/CategoryList'
import UserList from './pages/Users/UserList'
import Analytics from './pages/Analytics'
import Settings from './pages/Settings'

function App() {
  const { user, loading } = useAuth()

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    )
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={!user ? <AuthLayout><Login /></AuthLayout> : <Navigate to="/" />} />
        
        <Route path="/" element={user ? <AdminLayout /> : <Navigate to="/login" />}>
          <Route index element={<Dashboard />} />
          <Route path="movies" element={<MovieList />} />
          <Route path="movies/create" element={<MovieCreate />} />
          <Route path="movies/:id/edit" element={<MovieEdit />} />
          <Route path="categories" element={<CategoryList />} />
          <Route path="users" element={<UserList />} />
          <Route path="analytics" element={<Analytics />} />
          <Route path="settings" element={<Settings />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
