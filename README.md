# Riyobox Platform

Riyobox is a comprehensive movie streaming platform consisting of a Spring Boot backend, a React admin panel, and an Android client application.

## Project Structure

- `riyobox-backend/`: Spring Boot application providing the REST API and Socket.io services.
- `riyobox-admin/`: React + Vite dashboard for managing movies, categories, and users.
- `riyobox-android/`: Native Android application for users to stream and download movies.

## How it Works (Connections)

1.  **Backend & Admin:** The Admin panel connects to the backend using the `VITE_API_URL` environment variable. It uses this for CRUD operations on movies and for real-time statistics via Socket.io.
2.  **Backend & Android:** The Android app connects to the backend using the `RetrofitClient` configuration. It supports both REST API calls for data and Socket.io for real-time notifications.
3.  **Real-time Updates:** Both Admin and Android clients use Socket.io to communicate with the backend. When a new movie is added via the Admin panel, the backend broadcasts a `movie-added` event to all connected clients.

## Deployment Summary

- **Backend:** Deployed on **Render** using the provided `Dockerfile`.
- **Admin Panel:** Deployed on **Vercel** with automatic builds via **GitHub Actions**.
- **Android App:** Built via **GitHub Actions** which produces an APK on every push.
- **Database:** Requires a MongoDB instance (e.g., MongoDB Atlas) and a Redis instance.

For detailed deployment instructions, see the `README.md` files in each subdirectory.

## Quick Fixes Implemented

- Fixed naming conventions and typos across the project.
- Standardized API response format using a global wrapper.
- Switched WebSocket protocol from STOMP to Socket.io for compatibility across all platforms.
- Fixed critical bugs in Admin panel movie creation and file uploading logic.
- Implemented manual getters, setters, and builders for backend models to ensure consistent behavior.
