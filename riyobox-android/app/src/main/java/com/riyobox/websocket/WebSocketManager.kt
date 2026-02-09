package com.riyobox.websocket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.URISyntaxException

class WebSocketManager {
    private var socket: Socket? = null
    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState
    
    private val _realTimeStats = MutableStateFlow<Map<String, Any>>(emptyMap())
    val realTimeStats: StateFlow<Map<String, Any>> = _realTimeStats
    
    private val _newMovies = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val newMovies: StateFlow<List<Map<String, Any>>> = _newMovies
    
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
            
            socket?.on("stats-update") { args ->
                val data = args[0] as JSONObject
                _realTimeStats.value = data.toMap()
            }
            
            socket?.on("movie-added") { args ->
                val movie = args[0] as JSONObject
                // Handle new movie notification
                Log.d("WebSocket", "New movie added: ${movie.getString("title")}")
            }
            
            socket?.on("user-activity") { args ->
                val activity = args[0] as JSONObject
                Log.d("WebSocket", "User activity: $activity")
            }
            
            socket?.connect()
        } catch (e: URISyntaxException) {
            Log.e("WebSocket", "Connection error", e)
        }
    }
    
    fun disconnect() {
        socket?.disconnect()
        socket = null
        _connectionState.value = false
    }
    
    fun emit(event: String, data: Map<String, Any>) {
        socket?.emit(event, JSONObject(data))
    }
    
    companion object {
        private var instance: WebSocketManager? = null
        
        fun getInstance(): WebSocketManager {
            if (instance == null) {
                instance = WebSocketManager()
            }
            return instance!!
        }
    }
}

fun JSONObject.toMap(): Map<String, Any> = keys().asSequence().associate { key ->
    val value = this[key]
    key to when (value) {
        is JSONObject -> value.toMap()
        is org.json.JSONArray -> value.toList()
        else -> value
    }
}

fun org.json.JSONArray.toList(): List<Any> = (0 until length()).map { index ->
    when (val value = this[index]) {
        is JSONObject -> value.toMap()
        is org.json.JSONArray -> value.toList()
        else -> value
    }
}
