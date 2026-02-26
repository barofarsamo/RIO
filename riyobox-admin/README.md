# Riyobox Admin Panel

React-based administration dashboard for Riyobox.

## Deployment to Vercel

To deploy the admin panel to [Vercel](https://vercel.com):

1.  **Create a New Project:**
    - Connect your GitHub repository.
    - Select the `riyobox-admin` directory as the Root Directory.
    - Vercel will automatically detect the Vite configuration.

2.  **Configure Environment Variables:**
    Set the following environment variables in Vercel:
    - `VITE_API_URL`: The full URL of your deployed backend on Render (e.g., `https://riyobox-backend.onrender.com/api`).
    - `VITE_CLOUDINARY_CLOUD_NAME`: (Optional) If using Cloudinary for uploads.

3.  **Build Settings:**
    - Build Command: `npm run build`
    - Output Directory: `dist`

## CI/CD with GitHub Actions

The project includes a GitHub Actions workflow in `.github/workflows/riyobox-admin.yml` that automatically builds the project on every push to the `main` or `master` branch.

## Local Development

1.  Install dependencies: `npm install`
2.  Start dev server: `npm run dev`
3.  The admin will be available at `http://localhost:3000`.
