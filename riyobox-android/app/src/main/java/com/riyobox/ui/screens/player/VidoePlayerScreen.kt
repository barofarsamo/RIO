package com.riyobox.ui.screens.player

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.riyobox.ui.viewmodel.PlayerViewModel

class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val videoUrl = intent.getStringExtra("videoUrl") ?: ""
        val movieId = intent.getStringExtra("movieId") ?: ""
        
        setContent {
            VideoPlayerScreen(
                videoUrl = videoUrl,
                movieId = movieId,
                onBack = { finish() }
            )
        }
    }
}

@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    movieId: String,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }
    val player = remember { 
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> isLoading.value = false
                        Player.STATE_BUFFERING -> isLoading.value = true
                    }
                }
            })
        }
    }
    
    LaunchedEffect(videoUrl) {
        if (videoUrl.isNotBlank()) {
            val dataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent(Util.getUserAgent(context, "Riyobox"))
            
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            
            player.setMediaSource(mediaSource)
            player.prepare()
            player.playWhenReady = true
            
            // Record watch progress
            viewModel.recordWatch(movieId)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    setKeepContentOnPlayerReset(true)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
    
    DisposableEffect(Unit) {
        onDispose {
            player.release()
            viewModel.savePlaybackProgress(movieId, player.currentPosition)
        }
    }
}
