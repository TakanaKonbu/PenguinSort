package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.takanakonbu.penguinsort.sound.SoundManager

@Composable
fun MainScreen() {
    var gameStarted by remember { mutableStateOf(false) }
    var retryFromGameOver by remember { mutableStateOf(false) }

    // SoundManagerをMainScreenスコープで管理
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    // クリーンアップ処理
    DisposableEffect(Unit) {
        soundManager.startBgm() // アプリ起動時にBGMを開始
        onDispose {
            soundManager.release()
        }
    }

    if (gameStarted && !retryFromGameOver) {
        GameScreen(
            soundManager = soundManager, // SoundManagerを渡す
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