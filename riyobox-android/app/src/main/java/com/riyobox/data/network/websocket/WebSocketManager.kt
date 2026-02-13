package com.riyobox.data.network.websocket

import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.URISyntaxException

class WebSocketManager private constructor(context: Context) {
    private var socket: Socket? = null
    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState

    fun connect(token: String) {
        try {
            val options = IO.Options()
            options.auth = mapOf("token" to token)
            options.transports = arrayOf("websocket")

            socket = IO.socket("http://10.0.2.2:8080", options)

            socket?.on(Socket.EVENT_CONNECT) {
                _connectionState.value = true
                Log.d("WebSocket", "Connected")
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                _connectionState.value = false
                Log.d("WebSocket", "Disconnected")
            }

            socket?.connect()
        } catch (e: URISyntaxException) {
            Log.e("WebSocket", "Connection error", e)
        }
    }

    fun isConnected(): Boolean = _connectionState.value

    fun disconnect() {
        socket?.disconnect()
        socket = null
        _connectionState.value = false
    }

    companion object {
        private var instance: WebSocketManager? = null

        fun getInstance(context: Context): WebSocketManager {
            if (instance == null) {
                instance = WebSocketManager(context.applicationContext)
            }
            return instance!!
        }
    }
}
