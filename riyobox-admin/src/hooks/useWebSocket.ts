import { useEffect, useState, useCallback } from 'react'
import { io, Socket } from 'socket.io-client'

export const useWebSocket = (url: string) => {
  const [socket, setSocket] = useState<Socket | null>(null)
  const [isConnected, setIsConnected] = useState(false)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const newSocket = io(url, {
      auth: { token },
      transports: ['websocket'],
    })

    newSocket.on('connect', () => {
      setIsConnected(true)
      console.log('WebSocket connected')
    })

    newSocket.on('disconnect', () => {
      setIsConnected(false)
      console.log('WebSocket disconnected')
    })

    setSocket(newSocket)

    return () => {
      newSocket.close()
    }
  }, [url])

  const emit = useCallback((event: string, data: any) => {
    if (socket && isConnected) {
      socket.emit(event, data)
    }
  }, [socket, isConnected])

  const on = useCallback((event: string, callback: (data: any) => void) => {
    if (socket) {
      socket.on(event, callback)
    }
  }, [socket])

  const off = useCallback((event: string, callback: (data: any) => void) => {
    if (socket) {
      socket.off(event, callback)
    }
  }, [socket])

  return { socket, isConnected, emit, on, off }
}
