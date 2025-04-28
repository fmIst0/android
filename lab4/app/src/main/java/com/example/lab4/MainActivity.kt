package com.example.lab4

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.media.MediaPlayer
import android.view.View
import android.widget.EditText
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectFile: Button
    private lateinit var btnInputUrl: Button
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var playerView: PlayerView

    private var mediaPlayer: MediaPlayer? = null
    private var exoPlayer: ExoPlayer? = null
    private var selectedUri: Uri? = null
    private var isVideo: Boolean = false

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            selectedUri = it
            isVideo = checkIfVideo(uri.toString())
            prepareMedia()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelectFile = findViewById(R.id.btnSelectFile)
        btnInputUrl = findViewById(R.id.btnInputUrl)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnStop = findViewById(R.id.btnStop)
        playerView = findViewById(R.id.playerView)

        btnSelectFile.setOnClickListener { openFilePicker() }
        btnInputUrl.setOnClickListener { openUrlDialog() }
        btnPlay.setOnClickListener { playMedia() }
        btnPause.setOnClickListener { pauseMedia() }
        btnStop.setOnClickListener { stopMedia() }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        pickFileLauncher.launch(arrayOf("audio/*", "video/*"))
    }

    private fun openUrlDialog() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Введіть URL файлу")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val url = editText.text.toString()
                selectedUri = Uri.parse(url)
                isVideo = checkIfVideo(url)
                prepareMedia()
            }
            .setNegativeButton("Відмінити", null)
            .create()
        dialog.show()
    }

    private fun checkIfVideo(uri: String): Boolean {
        return uri.endsWith(".mp4") || uri.endsWith(".avi") || uri.endsWith(".mkv")
    }

    private fun prepareMedia() {
        releaseMedia()

        if (isVideo) {
            playerView.visibility = View.VISIBLE
            exoPlayer = ExoPlayer.Builder(this).build()
            playerView.player = exoPlayer
            val mediaItem = MediaItem.fromUri(selectedUri!!)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
        } else {
            playerView.visibility = View.GONE
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer?.setDataSource(this, selectedUri!!)
                mediaPlayer?.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun playMedia() {
        if (isVideo) {
            exoPlayer?.play()
        } else {
            mediaPlayer?.start()
        }
    }

    private fun pauseMedia() {
        if (isVideo) {
            exoPlayer?.pause()
        } else {
            mediaPlayer?.pause()
        }
    }

    private fun stopMedia() {
        if (isVideo) {
            exoPlayer?.stop()
            exoPlayer?.seekTo(0)
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.prepare()
        }
    }

    private fun releaseMedia() {
        mediaPlayer?.release()
        mediaPlayer = null
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMedia()
    }
}
