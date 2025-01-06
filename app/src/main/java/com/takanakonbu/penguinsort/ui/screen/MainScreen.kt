package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.runtime.*
import com.takanakonbu.penguinsort.sound.SoundManager
import com.takanakonbu.penguinsort.ui.components.AdManager

@Composable
fun MainScreen(adManager: AdManager, soundManager: SoundManager) {
    var gameStarted by remember { mutableStateOf(false) }
    var retryFromGameOver by remember { mutableStateOf(false) }

    // クリーンアップ処理
    DisposableEffect(Unit) {
        soundManager.startBgm() // アプリ起動時にBGMを開始
        onDispose {
            soundManager.release()
        }
    }

    if (gameStarted && !retryFromGameOver) {
        GameScreen(
            soundManager = soundManager,
            adManager = adManager,
            onRetry = {
                gameStarted = false
                retryFromGameOver = true
            }
        )
    } else {
        StartScreen(
            onStartClick = {
                gameStarted = true
                retryFromGameOver = false
            }
        )
    }
}