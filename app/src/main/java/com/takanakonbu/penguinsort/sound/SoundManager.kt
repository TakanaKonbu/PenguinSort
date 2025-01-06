package com.takanakonbu.penguinsort.sound

import android.content.Context
import android.media.MediaPlayer
import com.takanakonbu.penguinsort.R

class SoundManager(private val context: Context) {
    private var bgmPlayer: MediaPlayer? = null
    private var putPlayer: MediaPlayer? = null
    private var correctPlayer: MediaPlayer? = null
    private var gameOverPlayer: MediaPlayer? = null

    init {
        initializePlayers()
    }

    private fun initializePlayers() {
        // BGM用のMediaPlayer
        bgmPlayer = MediaPlayer.create(context, R.raw.bgm).apply {
            isLooping = true
            setVolume(0.5f, 0.5f)
        }

        // 配置音用のMediaPlayer
        putPlayer = MediaPlayer.create(context, R.raw.put)

        // 正解音用のMediaPlayer
        correctPlayer = MediaPlayer.create(context, R.raw.correct)

        // ゲームオーバー音用のMediaPlayer
        gameOverPlayer = MediaPlayer.create(context, R.raw.gameover)
    }

    fun startBgm() {
        bgmPlayer?.apply {
            if (!isPlaying) {
                start()
            }
        }
    }

    fun stopBgm() {
        bgmPlayer?.apply {
            if (isPlaying) {
                pause()
                seekTo(0)
            }
        }
    }

    fun playPutSound() {
        putPlayer?.apply {
            seekTo(0)
            start()
        }
    }

    fun playCorrectSound() {
        correctPlayer?.apply {
            seekTo(0)
            start()
        }
    }

    fun playGameOverSound() {
        gameOverPlayer?.apply {
            seekTo(0)
            start()
        }
    }

    fun release() {
        bgmPlayer?.apply {
            stop()
            release()
        }
        putPlayer?.release()
        correctPlayer?.release()
        gameOverPlayer?.release()

        bgmPlayer = null
        putPlayer = null
        correctPlayer = null
        gameOverPlayer = null
    }
}