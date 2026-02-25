import { useAuth as useAuthContext, AuthContextType } from '../contexts/AuthContext'

export const useAuth = (): AuthContextType => {
  return useAuthContext()
}
