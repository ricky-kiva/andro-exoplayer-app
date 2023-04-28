package com.rickyslash.exoplayerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.rickyslash.exoplayerapp.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {

    // `lazy` will access the value only when it's first accessed
    // it's not thread-safe because viewBinding won't be accessed by multiple threads
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // Build exoplayer & assign to View
        val player = ExoPlayer.Builder(this).build()
        viewBinding.exoPlayerview.player = player

        // set media that needs to be played
        val mediaItem = MediaItem.fromUri(URL_VIDEO_DICODING)
        player.setMediaItem(mediaItem)

        // player starts loading the media
        player.prepare()
    }
    
    companion object {
        const val URL_VIDEO_DICODING = "https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4"
    }
}