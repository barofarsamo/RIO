package com.riyobox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.riyobox.data.local.datastore.AppPreferences
import com.riyobox.data.network.websocket.WebSocketManager
import com.riyobox.ui.navigation.AppNavigation
import com.riyobox.ui.theme.RiyoboxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferences: AppPreferences
    
    private lateinit var webSocketManager: WebSocketManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize WebSocket connection
        webSocketManager = WebSocketManager.getInstance(applicationContext)
        
        // Connect WebSocket if user is logged in
        val token = preferences.getAuthToken()
        if (token != null) {
            webSocketManager.connect(token)
        }
        
        setContent {
            RiyoboxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Reconnect WebSocket when app comes to foreground
        val token = preferences.getAuthToken()
        if (token != null && !webSocketManager.isConnected()) {
            webSocketManager.connect(token)
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Optional: Disconnect when app goes to background to save battery
        // webSocketManager.disconnect()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up WebSocket connection
        webSocketManager.disconnect()
    }
}
