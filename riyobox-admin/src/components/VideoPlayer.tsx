import React, { useState } from 'react'
import ReactPlayer from 'react-player'
import { Play, Pause, Volume2 } from 'lucide-react'

interface VideoPlayerProps {
  url: string
  width?: string
  height?: string
}

const VideoPlayer: React.FC<VideoPlayerProps> = ({ 
  url, 
  width = '100%', 
  height = '300px' 
}) => {
  const [playing, setPlaying] = useState(false)
  const [volume, setVolume] = useState(0.8)
  const [played, setPlayed] = useState(0)
  const [duration, setDuration] = useState(0)

  const handlePlayPause = () => {
    setPlaying(!playing)
  }

  const handleProgress = (state: any) => {
    setPlayed(state.played)
  }

  const handleDuration = (duration: number) => {
    setDuration(duration)
  }

  const formatTime = (seconds: number) => {
    const date = new Date(seconds * 1000)
    const hh = date.getUTCHours()
    const mm = date.getUTCMinutes()
    const ss = date.getUTCSeconds()
    
    if (hh) {
      return `${hh}:${mm.toString().padStart(2, '0')}:${ss.toString().padStart(2, '0')}`
    }
    return `${mm}:${ss.toString().padStart(2, '0')}`
  }

  return (
    <div className="bg-black rounded-lg overflow-hidden">
      <div className="relative" style={{ width, height }}>
        <ReactPlayer
          url={url}
          playing={playing}
          volume={volume}
          width="100%"
          height="100%"
          onProgress={handleProgress}
          onDuration={handleDuration}
        />
        
        {/* Controls */}
        <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/80 to-transparent p-4">
          {/* Progress Bar */}
          <div className="w-full bg-gray-700 h-1 rounded-full mb-3">
            <div 
              className="bg-red-600 h-1 rounded-full"
              style={{ width: `${played * 100}%` }}
            ></div>
          </div>
          
          {/* Control Buttons */}
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <button
                onClick={handlePlayPause}
                className="p-2 bg-red-600 rounded-full hover:bg-red-700"
              >
                {playing ? (
                  <Pause className="w-4 h-4 text-white" />
                ) : (
                  <Play className="w-4 h-4 text-white" />
                )}
              </button>
              
              <div className="flex items-center space-x-2">
                <Volume2 className="w-4 h-4 text-white" />
                <input
                  type="range"
                  min="0"
                  max="1"
                  step="0.1"
                  value={volume}
                  onChange={(e) => setVolume(parseFloat(e.target.value))}
                  className="w-20 accent-red-600"
                />
              </div>
            </div>
            
            <div className="text-white text-sm">
              {formatTime(played * duration)} / {formatTime(duration)}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default VideoPlayer
