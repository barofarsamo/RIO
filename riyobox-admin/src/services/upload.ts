import api from './api'

export interface CloudflareR2Config {
  accountId: string
  accessKeyId: string
  secretAccessKey: string
  bucketName: string
  publicUrl: string
  region?: string
}

export interface UploadProgressEvent {
  progress: number
  loaded: number
  total: number
  chunk?: number
  totalChunks?: number
}

export interface UploadOptions {
  provider?: 'r2' | 'cloudinary' | 'backblaze'
  folder?: string
  category?: 'videos' | 'thumbnails' | 'posters' | 'subtitles'
  onProgress?: (event: UploadProgressEvent) => void
  maxRetries?: number
  chunkSize?: number
}

export interface PresignedResponse {
  uploadUrl: string
  publicUrl: string
  uploadId?: string
  expiresAt?: string
}

export interface MultipartPart {
  partNumber: number
  etag: string
  size: number
}

export const uploadService = {
  // Get R2 configuration from backend
  async getR2Config(): Promise<CloudflareR2Config> {
    const response = await api.get('/upload/config')
    return response.data
  },

  // Get upload configuration based on file type and size
  async getUploadConfig(file: File, options?: UploadOptions): Promise<{
    provider: string
    useMultipart: boolean
    chunkSize: number
  }> {
    const LARGE_FILE_THRESHOLD = 100 * 1024 * 1024 // 100MB

    const response = await api.post('/upload/config/file', {
      fileName: file.name,
      fileType: file.type,
      fileSize: file.size,
      category: options?.category || 'videos'
    })
    
    return response.data
  },

  // Upload file to R2 using presigned URL
  async uploadToR2(
    file: File, 
    config: CloudflareR2Config,
    options?: UploadOptions
  ): Promise<string> {
    try {
      const maxRetries = options?.maxRetries || 3
      let lastError: Error | null = null
      
      for (let attempt = 1; attempt <= maxRetries; attempt++) {
        try {
          // 1. Get presigned URL from backend
          const presignedResponse = await api.post('/upload/presigned', {
            fileName: this.generateUniqueFileName(file.name),
            fileType: file.type,
            fileSize: file.size,
            category: options?.category || 'videos'
          })
          
          const { uploadUrl, publicUrl } = presignedResponse.data
          
          // 2. Upload file directly to R2
          const controller = new AbortController()
          const timeoutId = setTimeout(() => controller.abort(), 300000) // 5 minute timeout
          
          const uploadResponse = await fetch(uploadUrl, {
            method: 'PUT',
            body: file,
            headers: {
              'Content-Type': file.type,
              'Content-Length': file.size.toString(),
              'x-amz-meta-filename': encodeURIComponent(file.name),
              'x-amz-meta-category': options?.category || 'videos',
              'x-amz-meta-original-size': file.size.toString(),
            },
            signal: controller.signal
          })
          
          clearTimeout(timeoutId)
          
          if (!uploadResponse.ok) {
            throw new Error(`Upload failed: ${uploadResponse.status} ${uploadResponse.statusText}`)
          }
          
          // 3. Verify upload
          const verifyResponse = await api.post('/upload/verify', {
            url: publicUrl,
            fileSize: file.size,
            fileType: file.type,
            etag: uploadResponse.headers.get('etag')
          })
          
          if (!verifyResponse.data.verified) {
            throw new Error('Upload verification failed')
          }
          
          // 4. Notify backend about successful upload
          await api.post('/upload/complete', {
            url: publicUrl,
            fileName: file.name,
            fileType: file.type,
            fileSize: file.size,
            category: options?.category || 'videos'
          })
          
          return publicUrl
          
        } catch (error) {
          lastError = error as Error
          console.warn(`Upload attempt ${attempt} failed:`, error)
          
          if (attempt < maxRetries) {
            // Exponential backoff
            await new Promise(resolve => 
              setTimeout(resolve, 1000 * Math.pow(2, attempt))
            )
            continue
          }
        }
      }
      
      throw lastError || new Error('Upload failed after all retries')
      
    } catch (error) {
      console.error('R2 upload error:', error)
      throw error
    }
  },

  // Upload file to Cloudinary
  async uploadToCloudinary(
    file: File, 
    folder: string = 'riyobox',
    options?: UploadOptions
  ): Promise<string> {
    try {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('upload_preset', 'riyobox_uploads')
      formData.append('folder', folder)
      
      if (options?.category) {
        formData.append('context', `category=${options.category}`)
      }
      
      // Add additional parameters for video optimization
      if (file.type.startsWith('video/')) {
        formData.append('resource_type', 'video')
        formData.append('eager', 'sp_full_hd/mp4,sp_hd/mp4,sp_sd/mp4')
        formData.append('eager_async', 'true')
      }
      
      const response = await fetch(
        `https://api.cloudinary.com/v1_1/${import.meta.env.VITE_CLOUDINARY_CLOUD_NAME}/upload`,
        {
          method: 'POST',
          body: formData,
        }
      )
      
      if (!response.ok) {
        throw new Error(`Cloudinary upload failed: ${response.statusText}`)
      }
      
      const data = await response.json()
      return data.secure_url
      
    } catch (error) {
      console.error('Cloudinary upload error:', error)
      throw error
    }
  },

  // Multipart upload for large files
  async uploadLargeFile(
    file: File, 
    config: CloudflareR2Config,
    options?: UploadOptions
  ): Promise<string> {
    const CHUNK_SIZE = options?.chunkSize || 10 * 1024 * 1024 // 10MB chunks
    const totalChunks = Math.ceil(file.size / CHUNK_SIZE)
    
    const onProgress = options?.onProgress || (() => {})
    
    try {
      // 1. Initiate multipart upload
      const initResponse = await api.post('/upload/multipart/initiate', {
        fileName: this.generateUniqueFileName(file.name),
        fileType: file.type,
        fileSize: file.size,
        category: options?.category || 'videos'
      })
      
      const { uploadId } = initResponse.data
      
      // 2. Upload parts
      const uploadParts: MultipartPart[] = []
      
      for (let chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
        const start = chunkIndex * CHUNK_SIZE
        const end = Math.min(start + CHUNK_SIZE, file.size)
        const chunk = file.slice(start, end)
        const partNumber = chunkIndex + 1
        
        let uploadSuccess = false
        let partError: Error | null = null
        
        // Retry each part up to 3 times
        for (let attempt = 1; attempt <= 3; attempt++) {
          try {
            // Get presigned URL for this part
            const partResponse = await api.post('/upload/multipart/part', {
              uploadId: uploadId,
              partNumber: partNumber,
              totalParts: totalChunks
            })
            
            const { partUrl } = partResponse.data
            
            // Upload the chunk
            const chunkResponse = await fetch(partUrl, {
              method: 'PUT',
              body: chunk,
              headers: { 
                'Content-Type': file.type,
                'Content-Length': chunk.size.toString()
              }
            })
            
            if (!chunkResponse.ok) {
              throw new Error(`Part ${partNumber} upload failed: ${chunkResponse.statusText}`)
            }
            
            const etag = chunkResponse.headers.get('etag')
            if (!etag) {
              throw new Error(`No ETag received for part ${partNumber}`)
            }
            
            uploadParts.push({
              partNumber: partNumber,
              etag: etag,
              size: chunk.size
            })
            
            uploadSuccess = true
            break
            
          } catch (error) {
            partError = error as Error
            console.warn(`Part ${partNumber} attempt ${attempt} failed:`, error)
            
            if (attempt < 3) {
              await new Promise(resolve => setTimeout(resolve, 1000 * attempt))
              continue
            }
          }
        }
        
        if (!uploadSuccess) {
          throw new Error(`Failed to upload part ${partNumber}: ${partError?.message}`)
        }
        
        // Update progress
        onProgress({
          progress: Math.round(((chunkIndex + 1) / totalChunks) * 100),
          loaded: (chunkIndex + 1) * CHUNK_SIZE,
          total: file.size,
          chunk: chunkIndex + 1,
          totalChunks: totalChunks
        })
      }
      
      // 3. Complete multipart upload
      const completeResponse = await api.post('/upload/multipart/complete', {
        uploadId: uploadId,
        parts: uploadParts
      })
      
      const { url, etag } = completeResponse.data
      
      // 4. Verify completed upload
      const verifyResponse = await api.post('/upload/multipart/verify', {
        uploadId: uploadId,
        etag: etag,
        fileSize: file.size
      })
      
      if (!verifyResponse.data.verified) {
        throw new Error('Multipart upload verification failed')
      }
      
      return url
      
    } catch (error) {
      console.error('Multipart upload error:', error)
      
      // Abort multipart upload on error
      try {
        await api.post('/upload/multipart/abort', {
          uploadId: (error as any).uploadId || ''
        })
      } catch (abortError) {
        console.error('Failed to abort multipart upload:', abortError)
      }
      
      throw error
    }
  },

  // Main upload method with automatic provider selection
  async uploadFile(
    file: File, 
    options: UploadOptions = {}
  ): Promise<string> {
    const provider = options.provider || 'r2'
    
    // Determine if we need multipart upload
    const config = await this.getUploadConfig(file, options)
    
    if (provider === 'cloudinary') {
      return this.uploadToCloudinary(file, options.folder, options)
    } else {
      const r2Config = await this.getR2Config()
      
      if (config.useMultipart && file.size > (options.chunkSize || 10 * 1024 * 1024)) {
        return this.uploadLargeFile(file, r2Config, options)
      } else {
        return this.uploadToR2(file, r2Config, options)
      }
    }
  },

  // Upload multiple files
  async uploadFiles(
    files: File[],
    options: UploadOptions = {}
  ): Promise<string[]> {
    const uploadPromises = files.map(file => this.uploadFile(file, options))
    return Promise.all(uploadPromises)
  },

  // Delete uploaded file
  async deleteFile(url: string): Promise<void> {
    await api.delete('/upload/delete', {
      data: { url }
    })
  },

  // Get upload status
  async getUploadStatus(uploadId: string): Promise<{
    status: 'pending' | 'uploading' | 'completed' | 'failed'
    progress?: number
    url?: string
    error?: string
  }> {
    const response = await api.get(`/upload/status/${uploadId}`)
    return response.data
  },

  // Generate unique filename
  private generateUniqueFileName(originalName: string): string {
    const timestamp = Date.now()
    const randomString = Math.random().toString(36).substring(2, 15)
    const extension = originalName.split('.').pop()
    const nameWithoutExt = originalName.substring(0, originalName.lastIndexOf('.'))

    return `${nameWithoutExt}-${timestamp}-${randomString}.${extension}`
  },

  // Validate file before upload
  validateFile(file: File, options?: UploadOptions): { valid: boolean; errors: string[] } {
    const errors: string[] = []
    const maxSize = options?.category === 'videos' ? 2 * 1024 * 1024 * 1024 : 50 * 1024 * 1024 // 2GB for videos, 50MB for others
    
    // Check file size
    if (file.size > maxSize) {
      errors.push(`File too large. Maximum size is ${this.formatBytes(maxSize)}`)
    }
    
    // Check file type
    if (options?.category === 'videos') {
      const allowedVideoTypes = ['video/mp4', 'video/mkv', 'video/webm', 'video/avi', 'video/mov']
      if (!allowedVideoTypes.includes(file.type)) {
        errors.push('Invalid video format. Allowed: MP4, MKV, WebM, AVI, MOV')
      }
    } else if (options?.category === 'thumbnails' || options?.category === 'posters') {
      const allowedImageTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif']
      if (!allowedImageTypes.includes(file.type)) {
        errors.push('Invalid image format. Allowed: JPEG, PNG, WebP, GIF')
      }
    }
    
    return {
      valid: errors.length === 0,
      errors
    }
  },

  // Helper to format bytes
  private formatBytes(bytes: number, decimals: number = 2): string {
    if (bytes === 0) return '0 Bytes'

    const k = 1024
    const dm = decimals < 0 ? 0 : decimals
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB']

    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i]
  }
}
