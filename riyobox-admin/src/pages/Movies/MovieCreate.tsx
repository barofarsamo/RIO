import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { movieService, uploadService } from '../../services'
import FileUpload from '../../components/FileUpload'
import VideoPlayer from '../../components/VideoPlayer'
import toast from 'react-hot-toast'

const movieSchema = z.object({
  title: z.string().min(1, 'Title is required'),
  description: z.string().min(10, 'Description must be at least 10 characters'),
  duration: z.number().min(1, 'Duration must be positive'),
  releaseYear: z.number().min(1900).max(new Date().getFullYear() + 1),
  categories: z.array(z.string()).min(1, 'Select at least one category'),
  actors: z.array(z.string()).min(1, 'Add at least one actor'),
  director: z.string().min(1, 'Director is required'),
  isFeatured: z.boolean().default(false),
  isSomaliOriginal: z.boolean().default(false),
})

type MovieFormData = z.infer<typeof movieSchema>

const MovieCreate: React.FC = () => {
  const navigate = useNavigate()
  const [thumbnailUrl, setThumbnailUrl] = useState('')
  const [videoUrl, setVideoUrl] = useState('')
  const [uploading, setUploading] = useState(false)
  const [uploadProgress, setUploadProgress] = useState(0)
  
  const { register, handleSubmit, formState: { errors }, setValue, watch } = useForm<MovieFormData>({
    resolver: zodResolver(movieSchema),
    defaultValues: {
      categories: [],
      actors: [],
      isFeatured: false,
      isSomaliOriginal: false,
    },
  })
  
  const categories = [
    'Action', 'Comedy', 'Drama', 'Horror', 'Romance',
    'Somali Originals', 'Documentary', 'Musical', 'Thriller', 'Family'
  ]
  
  const handleThumbnailUpload = async (file: File) => {
    if (!file.name) return
    try {
      setUploading(true)
      const url = await uploadService.uploadFile(file, { category: 'thumbnails' })
      setThumbnailUrl(url)
      toast.success('Thumbnail uploaded successfully')
    } catch (error) {
      toast.error('Failed to upload thumbnail')
    } finally {
      setUploading(false)
    }
  }
  
  const handleVideoUpload = async (file: File) => {
    try {
      setUploading(true)
      setUploadProgress(0)
      
      const url = await uploadService.uploadFile(file, {
        category: 'videos',
        onProgress: (e) => setUploadProgress(e.progress)
      })
      
      setUploadProgress(100)
      setVideoUrl(url)
      toast.success('Video uploaded successfully')
    } catch (error) {
      toast.error('Failed to upload video')
    } finally {
      setUploading(false)
    }
  }
  
  const onSubmit = async (data: MovieFormData) => {
    if (!thumbnailUrl || !videoUrl) {
      toast.error('Please upload both thumbnail and video')
      return
    }
    
    try {
      const movieData = {
        ...data,
        thumbnailUrl,
        videoUrl,
      }
      
      await movieService.createMovie(movieData)
      toast.success('Movie created successfully')
      navigate('/movies')
    } catch (error) {
      toast.error('Failed to create movie')
    }
  }
  
  const addActor = () => {
    const actorInput = document.getElementById('actor-input') as HTMLInputElement
    if (actorInput?.value.trim()) {
      const currentActors = watch('actors')
      setValue('actors', [...currentActors, actorInput.value.trim()])
      actorInput.value = ''
    }
  }
  
  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Add New Movie</h1>
        <p className="text-gray-600">Upload and manage movie details</p>
      </div>
      
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column - Uploads */}
          <div className="lg:col-span-1 space-y-6">
            {/* Thumbnail Upload */}
            <div className="bg-white rounded-xl shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Thumbnail</h3>
              <FileUpload
                accept="image/*"
                onUpload={handleThumbnailUpload}
                previewUrl={thumbnailUrl}
                uploading={uploading}
              />
            </div>
            
            {/* Video Upload */}
            <div className="bg-white rounded-xl shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Video</h3>
              <FileUpload
                accept="video/*"
                onUpload={handleVideoUpload}
                uploading={uploading}
                showProgress={true}
                progress={uploadProgress}
              />
              {videoUrl && (
                <div className="mt-4">
                  <VideoPlayer url={videoUrl} />
                </div>
              )}
            </div>
          </div>
          
          {/* Right Column - Form */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white rounded-xl shadow p-6">
              {/* Title & Description */}
              <div className="space-y-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Movie Title *
                  </label>
                  <input
                    type="text"
                    {...register('title')}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
                    placeholder="Enter movie title"
                  />
                  {errors.title && (
                    <p className="mt-2 text-sm text-red-600">{errors.title.message}</p>
                  )}
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Description *
                  </label>
                  <textarea
                    {...register('description')}
                    rows={4}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
                    placeholder="Enter movie description"
                  />
                  {errors.description && (
                    <p className="mt-2 text-sm text-red-600">{errors.description.message}</p>
                  )}
                </div>
              </div>
              
              {/* Duration & Release Year */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Duration (minutes) *
                  </label>
                  <input
                    type="number"
                    {...register('duration', { valueAsNumber: true })}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
                    placeholder="120"
                  />
                  {errors.duration && (
                    <p className="mt-2 text-sm text-red-600">{errors.duration.message}</p>
                  )}
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Release Year *
                  </label>
                  <input
                    type="number"
                    {...register('releaseYear', { valueAsNumber: true })}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
                    placeholder="2023"
                  />
                  {errors.releaseYear && (
                    <p className="mt-2 text-sm text-red-600">{errors.releaseYear.message}</p>
                  )}
                </div>
              </div>
              
              {/* Director */}
              <div className="mt-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Director *
                </label>
                <input
                  type="text"
                  {...register('director')}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-transparent"
                  placeholder="Enter director name"
                />
                {errors.director && (
                  <p className="mt-2 text-sm text-red-600">{errors.director.message}</p>
                )}
              </div>
              
              {/* Categories */}
              <div className="mt-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Categories *
                </label>
                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-2">
                  {categories.map((category) => {
                    const currentCategories = watch('categories')
                    const isSelected = currentCategories.includes(category)
                    
                    return (
                      <button
                        key={category}
                        type="button"
                        onClick={() => {
                          if (isSelected) {
                            setValue('categories', currentCategories.filter(c => c !== category))
                          } else {
                            setValue('categories', [...currentCategories, category])
                          }
                        }}
                        className={`px-3 py-2 rounded-lg border text-sm ${
                          isSelected
                            ? 'bg-red-50 border-red-500 text-red-700'
                            : 'border-gray-300 text-gray-700 hover:bg-gray-50'
                        }`}
                      >
                        {category}
                      </button>
                    )
                  })}
                </div>
                {errors.categories && (
                  <p className="mt-2 text-sm text-red-600">{errors.categories.message}</p>
                )}
              </div>
              
              {/* Actors */}
              <div className="mt-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Actors *
                </label>
                <div className="flex space-x-2 mb-3">
                  <input
                    id="actor-input"
                    type="text"
                    className="flex-1 px-4 py-2 border border-gray-300 rounded-lg"
                    placeholder="Add actor name"
                  />
                  <button
                    type="button"
                    onClick={addActor}
                    className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200"
                  >
                    Add
                  </button>
                </div>
                
                <div className="flex flex-wrap gap-2">
                  {watch('actors').map((actor, index) => (
                    <div
                      key={index}
                      className="inline-flex items-center px-3 py-1 bg-gray-100 text-gray-700 rounded-lg"
                    >
                      <span>{actor}</span>
                      <button
                        type="button"
                        onClick={() => {
                          const currentActors = watch('actors')
                          setValue('actors', currentActors.filter((_, i) => i !== index))
                        }}
                        className="ml-2 text-gray-500 hover:text-gray-700"
                      >
                        ×
                      </button>
                    </div>
                  ))}
                </div>
                {errors.actors && (
                  <p className="mt-2 text-sm text-red-600">{errors.actors.message}</p>
                )}
              </div>
              
              {/* Flags */}
              <div className="mt-6 space-y-3">
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    id="isFeatured"
                    {...register('isFeatured')}
                    className="w-4 h-4 text-red-600 border-gray-300 rounded focus:ring-red-500"
                  />
                  <label htmlFor="isFeatured" className="ml-2 text-gray-700">
                    Mark as Featured Movie
                  </label>
                </div>
                
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    id="isSomaliOriginal"
                    {...register('isSomaliOriginal')}
                    className="w-4 h-4 text-red-600 border-gray-300 rounded focus:ring-red-500"
                  />
                  <label htmlFor="isSomaliOriginal" className="ml-2 text-gray-700">
                    Mark as Somali Original
                  </label>
                </div>
              </div>
            </div>
            
            {/* Submit Buttons */}
            <div className="flex justify-end space-x-4">
              <button
                type="button"
                onClick={() => navigate('/movies')}
                className="px-6 py-3 border border-gray-300 text-gray-700 font-medium rounded-lg hover:bg-gray-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={uploading}
                className="px-6 py-3 bg-red-600 text-white font-medium rounded-lg hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {uploading ? 'Saving...' : 'Create Movie'}
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  )
}

export default MovieCreate
