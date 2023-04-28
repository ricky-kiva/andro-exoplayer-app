package com.rickyslash.exoplayerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util
import com.rickyslash.exoplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // `lazy` will access the value only when it's first accessed
    // it's not thread-safe because viewBinding won't be accessed by multiple threads
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // Build exoplayer & assign to View
        val player = ExoPlayer.Builder(this).build()
        viewBinding.exoPlayerview.player = player

        // set media that needs to be played
        val mediaItem = MediaItem.fromUri(URL_VIDEO_DICODING)
        val anotherMediaItem = MediaItem.fromUri(URL_AUDIO)

        // add media to exoplayer's playlist
        player.setMediaItem(mediaItem)
        player.addMediaItem(anotherMediaItem)

        // player starts loading the media
        player.prepare()
    }

    override fun onStart() {
        super.onStart()
        // this to support multiple windows in API level 24 & above
        // because app is seen but not active on split windows mode
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        // on API level 23 and below, app need to wait for the resource to be fulfilled
        // so it's called on onResume
        if (Util.SDK_INT <= 23 && player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        // on API level 23, onStop() not guaranteed. So it needs to be dispatched by onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        // on API level 24 & above, onStop() is guaranteed
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        // add media to exoplayer's playlist
        val mediaItem = MediaItem.fromUri(URL_VIDEO_DICODING)
        val anotherMediaItem = MediaItem.fromUri(URL_AUDIO)

        // Build exoplayer
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->

            // assign to View
            viewBinding.exoPlayerview.player = exoPlayer

            // add media to exoplayer's playlist
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.addMediaItem(anotherMediaItem)

            // player starts loading the media
            exoPlayer.prepare()
        }
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun hideSystemUI() {
        // this to make the systemUI overlays not overlap the content of the window
        // systemUI overlays are the elements that appear on top of application's content (status bar & nav bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // this control systemUI behavior of the window
        WindowInsetsControllerCompat(window, viewBinding.exoPlayerview).let { controller ->
            // this to hide systemBars
            controller.hide(WindowInsetsCompat.Type.systemBars())
            // this to show the system bar when user swipe on the corresponding area
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    
    companion object {
        const val URL_VIDEO_DICODING = "https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4"
        const val URL_AUDIO = "https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3"
    }
}