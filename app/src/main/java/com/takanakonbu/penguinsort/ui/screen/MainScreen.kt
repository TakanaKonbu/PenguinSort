package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.runtime.*
import com.takanakonbu.penguinsort.sound.SoundManager
import com.takanakonbu.penguinsort.ui.components.AdManager

@Composable
fun MainScreen(adManager: AdManager, soundManager: SoundManager) {
    var gameStarted by remember { mutableStateOf(false) }
    var retryFromGameOver by remember { mutableStateOf(false) }
    var scoreUpdateTrigger by remember { mutableStateOf(0) }

    if (gameStarted && !retryFromGameOver) {
        GameScreen(
            soundManager = soundManager,
            adManager = adManager,
            onRetry = {
                gameStarted = false
                retryFromGameOver = true
                // ゲーム終了時にトリガーを更新
                scoreUpdateTrigger += 1
            }
        )
    } else {
        StartScreen(
            onStartClick = {
                gameStarted = true
                retryFromGameOver = false
            },
            scoreUpdateTrigger = scoreUpdateTrigger
        )
    }
}