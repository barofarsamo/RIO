import React from 'react'

interface StatsCardProps {
  title: string
  value: string | number
  icon: React.ReactNode
  trend?: string
  trendColor?: string
}

const StatsCard: React.FC<StatsCardProps> = ({ title, value, icon, trend, trendColor }) => {
  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
      <div className="flex items-center justify-between mb-4">
        <div className="p-3 bg-gray-50 rounded-lg">{icon}</div>
        {trend && (
          <span className={`text-sm font-medium ${trendColor || 'text-green-600'}`}>
            {trend}
          </span>
        )}
      </div>
      <h3 className="text-gray-500 text-sm font-medium">{title}</h3>
      <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
    </div>
  )
}

export default StatsCard
