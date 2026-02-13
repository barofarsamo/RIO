package com.riyobox.data.network

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.riyobox.BuildConfig
import com.riyobox.data.local.datastore.AppPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(
    private val context: Context,
    private val preferences: AppPreferences
) {

    // Base URLs for different environments
    companion object {
        // Development
        private const val DEV_BASE_URL = "http://10.0.2.2:8080/api/" // Android emulator localhost
        
        // Production
        private const val PROD_BASE_URL = "https://api.riyobox.com/api/"
        
        // Staging
        private const val STAGING_BASE_URL = "https://staging.api.riyobox.com/api/"
        
        // Get base URL based on build type
        fun getBaseUrl(): String {
            return if (BuildConfig.DEBUG) DEV_BASE_URL else PROD_BASE_URL
        }
    }

    private fun getAuthToken(): String? {
        return preferences.getAuthToken()
    }

    // Common headers interceptor
    private val headersInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = getAuthToken()
        
        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("User-Agent", "Riyobox-Android/${BuildConfig.VERSION_NAME}")
            .header("X-Platform", "android")
            .header("X-Device-Id", getDeviceId())
            .header("X-App-Version", BuildConfig.VERSION_NAME)
            .header("X-OS-Version", android.os.Build.VERSION.RELEASE)
            .header("X-Device-Model", android.os.Build.MODEL)
        
        // Add authorization header if token exists
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        
        // Add range headers for video streaming
        if (original.url.toString().contains("/stream/") || original.url.toString().contains("/video/")) {
            requestBuilder.header("Accept-Ranges", "bytes")
            requestBuilder.header("Range", "bytes=0-") // Default range for initial request
        }
        
        chain.proceed(requestBuilder.build())
    }

    // Authentication error interceptor
    private val authErrorInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        
        // Handle authentication errors (401 Unauthorized)
        if (response.code == 401) {
            // Clear auth token since it's invalid
            preferences.clearAuthToken()
            
            // You can trigger a global event here to navigate to login screen
            // For example using a shared flow or event bus
            // authEventChannel.trySend(Event.AuthenticationRequired)
        }
        
        // Handle rate limiting (429 Too Many Requests)
        if (response.code == 429) {
            // You can implement retry logic or show message to user
        }
        
        response
    }

    // Network logging interceptor (only in debug mode)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Offline cache interceptor (if you implement caching)
    private val cacheInterceptor = Interceptor { chain ->
        var request = chain.request()
        // Check if network is available
        val isNetworkAvailable = isNetworkAvailable(context)
        
        request = if (isNetworkAvailable) {
            request.newBuilder()
                .header("Cache-Control", "public, max-age=60") // 1 minute cache
                .build()
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=604800") // 7 days
                .build()
        }
        
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        // Network interceptors (applied after retry)
        .addNetworkInterceptor(loggingInterceptor)
        .addNetworkInterceptor(cacheInterceptor)
        
        // Application interceptors (applied first)
        .addInterceptor(headersInterceptor)
        .addInterceptor(authErrorInterceptor)
        
        // Connection timeouts
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS) // Longer timeout for video streaming
        .writeTimeout(30, TimeUnit.SECONDS)
        
        // Connection pool configuration
        .connectionPool(okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES))
        
        // Retry on connection failure
        .retryOnConnectionFailure(true)
        
        // Enable HTTP/2 and HTTP/1.1 fallback
        .protocols(listOf(okhttp3.Protocol.HTTP_2, okhttp3.Protocol.HTTP_1_1))
        
        // Cookie management (if needed)
        // .cookieJar(cookieJar)
        
        .build()

    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .serializeNulls()
        .setLenient() // Be lenient with JSON parsing
        .create()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    // Create a separate client for video streaming with different timeouts
    val videoOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headersInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS) // Very long timeout for video streaming
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Create ExoPlayer data source factory for video streaming
    fun createExoPlayerDataSourceFactory(): DataSource.Factory {
        val userAgent = Util.getUserAgent(context, "Riyobox-Player")
        val token = getAuthToken()
        
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(userAgent)
            .setConnectTimeoutMs(30000)
            .setReadTimeoutMs(120000) // 2 minutes for video
            .setAllowCrossProtocolRedirects(true)
        
        // Add headers for authentication
        token?.let {
            defaultHttpDataSourceFactory.setDefaultRequestProperties(
                mapOf(
                    "Authorization" to "Bearer $it",
                    "Accept-Ranges" to "bytes"
                )
            )
        }
        
        return defaultHttpDataSourceFactory
    }

    // Create ExoPlayer data source factory for downloading
    fun createDownloadDataSourceFactory(): DataSource.Factory {
        val userAgent = Util.getUserAgent(context, "Riyobox-Downloader")
        val token = getAuthToken()
        
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(userAgent)
            .setConnectTimeoutMs(60000) // 1 minute
            .setReadTimeoutMs(300000) // 5 minutes for downloads
            .setAllowCrossProtocolRedirects(true)
        
        token?.let {
            defaultHttpDataSourceFactory.setDefaultRequestProperties(
                mapOf(
                    "Authorization" to "Bearer $it",
                    "Accept-Ranges" to "bytes"
                )
            )
        }
        
        return defaultHttpDataSourceFactory
    }

    // Helper function to get device ID
    private fun getDeviceId(): String {
        return android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        ) ?: "unknown-device"
    }

    // Check network availability
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as android.net.ConnectivityManager
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && (
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET)
            )
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    // Clear all caches
    fun clearCache() {
        okHttpClient.cache?.evictAll()
    }

    // Create a custom request for video streaming with range headers
    fun createStreamingRequest(url: String, rangeStart: Long = 0, rangeEnd: Long? = null): okhttp3.Request {
        val rangeHeader = if (rangeEnd != null) {
            "bytes=$rangeStart-$rangeEnd"
        } else {
            "bytes=$rangeStart-"
        }
        
        return okhttp3.Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ${getAuthToken()}")
            .header("Accept-Ranges", "bytes")
            .header("Range", rangeHeader)
            .header("User-Agent", "Riyobox-Streaming/${BuildConfig.VERSION_NAME}")
            .build()
    }
}
