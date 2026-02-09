import axios, { AxiosError, AxiosResponse } from 'axios'
import toast from 'react-hot-toast'

export interface ApiResponse<T = any> {
  success: boolean
  data?: T
  message?: string
  errors?: Record<string, string>
  timestamp: number
}

export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000, // 30 seconds
})

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // Add timestamp to avoid caching
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
api.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    // Check if response has our API format
    if (response.data && typeof response.data === 'object') {
      const apiResponse = response.data as ApiResponse
      
      // If API returned success=false, show error
      if (!apiResponse.success) {
        const errorMessage = apiResponse.message || 'Request failed'
        const errors = apiResponse.errors
        
        if (errors) {
          Object.values(errors).forEach(error => {
            toast.error(error)
          })
        } else {
          toast.error(errorMessage)
        }
        
        return Promise.reject(new Error(errorMessage))
      }
      
      // Return only the data for successful responses
      return {
        ...response,
        data: apiResponse.data
      }
    }
    
    return response
  },
  (error: AxiosError) => {
    if (error.response) {
      // Server responded with error
      const status = error.response.status
      const data = error.response.data as any
      
      switch (status) {
        case 401:
          // Unauthorized - clear token and redirect
          localStorage.removeItem('token')
          window.location.href = '/login'
          toast.error('Session expired. Please login again.')
          break
          
        case 403:
          toast.error('Access denied. You do not have permission.')
          break
          
        case 404:
          toast.error('Resource not found.')
          break
          
        case 409:
          toast.error(data?.message || 'Conflict occurred.')
          break
          
        case 422:
          // Validation errors
          if (data?.errors) {
            Object.values(data.errors).forEach((error: any) => {
              toast.error(error)
            })
          } else {
            toast.error(data?.message || 'Validation failed.')
          }
          break
          
        case 500:
          toast.error('Server error. Please try again later.')
          break
          
        default:
          toast.error(data?.message || 'An error occurred.')
      }
    } else if (error.request) {
      // No response received
      toast.error('Network error. Please check your connection.')
    } else {
      // Request setup error
      toast.error('Request error: ' + error.message)
    }
    
    return Promise.reject(error)
  }
)

// Helper functions
export const apiGet = async <T>(url: string, params?: any): Promise<T> => {
  const response = await api.get<ApiResponse<T>>(url, { params })
  return response.data as T
}

export const apiPost = async <T>(url: string, data?: any): Promise<T> => {
  const response = await api.post<ApiResponse<T>>(url, data)
  return response.data as T
}

export const apiPut = async <T>(url: string, data?: any): Promise<T> => {
  const response = await api.put<ApiResponse<T>>(url, data)
  return response.data as T
}

export const apiDelete = async <T>(url: string): Promise<T> => {
  const response = await api.delete<ApiResponse<T>>(url)
  return response.data as T
}

export default api
