# Riyobox Backend

Spring Boot backend for the Riyobox movie streaming platform.

## Deployment to Render

To deploy this backend to [Render](https://render.com), follow these steps:

1.  **Create a New Web Service:**
    - Connect your GitHub repository.
    - Select the `riyobox-backend` directory as the Root Directory.
    - Choose **Docker** as the Runtime.

2.  **Configure Environment Variables:**
    Set the following environment variables in the Render dashboard:
    - `MONGODB_URI`: Your MongoDB connection string (e.g., from MongoDB Atlas).
    - `REDIS_HOST`: Your Redis host.
    - `REDIS_PORT`: Your Redis port (usually 6379).
    - `REDIS_PASSWORD`: Your Redis password.
    - `JWT_SECRET`: A long, random string for security.
    - `CORS_ALLOWED_ORIGINS`: The URL of your deployed Admin panel (e.g., `https://riyobox-admin.vercel.app`).

3.  **Port Configuration:**
    Render will automatically detect the `EXPOSE 8080` in the Dockerfile. Ensure the `PORT` environment variable is set to `8080` if Render doesn't do it automatically.

4.  **Networking (Socket.io):**
    The backend starts a Socket.io server on port `9092`. Note that Render's free tier only exposes one public port. For production, you may need a Render paid plan or a reverse proxy setup if you want to access WebSockets on a different port.

## Local Development

1.  Ensure you have Java 17 and Maven installed.
2.  Run `mvn clean install`.
3.  Run `mvn spring-boot:run`.
