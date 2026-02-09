import React from 'react'
import { Link } from 'react-router-dom'

interface AuthLayoutProps {
  children: React.ReactNode
}

const AuthLayout: React.FC<AuthLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-black">
      <div className="flex flex-col lg:flex-row min-h-screen">
        {/* Left side - Brand */}
        <div className="lg:w-1/2 flex items-center justify-center p-8">
          <div className="max-w-md">
            <Link to="/" className="flex items-center space-x-4 mb-12">
              <div className="w-12 h-12 bg-red-600 rounded-xl flex items-center justify-center">
                <span className="text-white text-2xl font-bold">R</span>
              </div>
              <div>
                <h1 className="text-4xl font-bold text-white">Riyobox</h1>
                <p className="text-gray-400">Admin Panel</p>
              </div>
            </Link>
            
            <div className="space-y-6">
              <div className="flex items-center space-x-3 text-gray-300">
                <div className="w-8 h-8 bg-red-500/20 rounded-lg flex items-center justify-center">
                  <span className="text-red-400">🎬</span>
                </div>
                <span>Manage Movies & Content</span>
              </div>
              
              <div className="flex items-center space-x-3 text-gray-300">
                <div className="w-8 h-8 bg-red-500/20 rounded-lg flex items-center justify-center">
                  <span className="text-red-400">📊</span>
                </div>
                <span>Real-time Analytics</span>
              </div>
              
              <div className="flex items-center space-x-3 text-gray-300">
                <div className="w-8 h-8 bg-red-500/20 rounded-lg flex items-center justify-center">
                  <span className="text-red-400">⚡</span>
                </div>
                <span>Fast & Secure Dashboard</span>
              </div>
            </div>
            
            <div className="mt-12 p-6 bg-gray-800/50 rounded-xl">
              <p className="text-gray-400 italic">
                "Powerful admin panel for managing the Riyobox streaming platform with real-time updates and comprehensive analytics."
              </p>
            </div>
          </div>
        </div>

        {/* Right side - Auth Form */}
        <div className="lg:w-1/2 bg-white flex items-center justify-center p-8">
          <div className="w-full max-w-md">
            {children}
          </div>
        </div>
      </div>
    </div>
  )
}

export default AuthLayout
