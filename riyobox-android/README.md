# Riyobox Android App

Native Android application for the Riyobox streaming platform.

## Configuration

To connect the app to your deployed backend:

1.  Open `riyobox-android/app/src/main/java/com/riyobox/data/network/RetrofitClient.kt`.
2.  Update the `PROD_BASE_URL` with your Render backend URL:
    ```kotlin
    private const val PROD_BASE_URL = "https://your-backend-on-render.com/api/"
    ```

## CI/CD with GitHub Actions

The project includes a GitHub Actions workflow in `.github/workflows/riyobox-android.yml` that automatically builds the debug APK on every push. You can find the APK in the "Actions" tab of your GitHub repository.

## Features

- Movie streaming with ExoPlayer.
- Offline downloads.
- Real-time notifications via Socket.io.
- User authentication and profiles.

## Build Requirements

- Android Studio Hedgehog or newer.
- JDK 17.
- Gradle 8.2.
