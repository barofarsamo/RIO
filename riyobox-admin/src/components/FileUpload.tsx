import React, { useCallback } from 'react'
import { useDropzone } from 'react-dropzone'
import { Upload, X, Check } from 'lucide-react'

interface FileUploadProps {
  accept: string
  onUpload: (file: File) => Promise<void>
  previewUrl?: string
  uploading?: boolean
  showProgress?: boolean
  progress?: number
  maxSize?: number // in MB
}

const FileUpload: React.FC<FileUploadProps> = ({
  accept,
  onUpload,
  previewUrl,
  uploading = false,
  showProgress = false,
  progress = 0,
  maxSize = 100,
}) => {
  const onDrop = useCallback(async (acceptedFiles: File[]) => {
    if (acceptedFiles.length > 0) {
      await onUpload(acceptedFiles[0])
    }
  }, [onUpload])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: accept ? { [accept]: [] } : undefined,
    multiple: false,
    maxSize: maxSize * 1024 * 1024,
  })

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }

  return (
    <div>
      {previewUrl && accept.includes('image') ? (
        <div className="relative">
          <img
            src={previewUrl}
            alt="Preview"
            className="w-full h-64 object-cover rounded-lg"
          />
          <button
            type="button"
            onClick={() => onUpload(new File([], ''))}
            className="absolute top-2 right-2 p-1 bg-red-600 text-white rounded-full"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      ) : (
        <div
          {...getRootProps()}
          className={`border-2 border-dashed rounded-lg p-8 text-center cursor-pointer transition-colors ${
            isDragActive
              ? 'border-red-500 bg-red-50'
              : 'border-gray-300 hover:border-red-400 hover:bg-gray-50'
          } ${uploading ? 'opacity-50 cursor-not-allowed' : ''}`}
        >
          <input {...getInputProps()} disabled={uploading} />
          
          {uploading ? (
            <div className="space-y-4">
              <div className="flex justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-red-600"></div>
              </div>
              <p className="text-gray-600">Uploading...</p>
              {showProgress && (
                <div className="w-full bg-gray-200 rounded-full h-2.5">
                  <div
                    className="bg-red-600 h-2.5 rounded-full transition-all duration-300"
                    style={{ width: `${progress}%` }}
                  ></div>
                </div>
              )}
            </div>
          ) : (
            <>
              <div className="flex justify-center mb-4">
                <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center">
                  <Upload className="w-8 h-8 text-gray-500" />
                </div>
              </div>
              
              <p className="text-gray-700 mb-1">
                <span className="text-red-600 font-medium">Click to upload</span> or drag and drop
              </p>
              <p className="text-sm text-gray-500">
                {accept.includes('image') ? 'PNG, JPG, GIF up to ' : 'MP4, MOV up to '}
                {maxSize}MB
              </p>
              
              {isDragActive && (
                <div className="mt-4 p-2 bg-green-50 text-green-700 rounded">
                  <Check className="inline w-4 h-4 mr-1" />
                  Drop file here
                </div>
              )}
            </>
          )}
        </div>
      )}
    </div>
  )
}

export default FileUpload
