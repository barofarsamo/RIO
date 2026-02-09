import api from './api'
import { User, AuthResponse } from '../types'

export const authService = {
  async login(email: string, password: string): Promise<AuthResponse> {
    const response = await api.post('/auth/login', { email, password })
    return response.data
  },

  async register(email: string, password: string, name: string): Promise<AuthResponse> {
    const response = await api.post('/auth/register', { email, password, name })
    return response.data
  },

  async getProfile(): Promise<User> {
    const response = await api.get('/auth/profile')
    return response.data
  },

  async updateProfile(userId: string, data: Partial<User>): Promise<User> {
    const response = await api.put(`/users/${userId}`, data)
    return response.data
  },
}
