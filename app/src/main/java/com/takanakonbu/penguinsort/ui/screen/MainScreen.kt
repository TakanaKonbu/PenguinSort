package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.runtime.*

@Composable
fun MainScreen() {
    var gameStarted by remember { mutableStateOf(false) }
    var retryFromGameOver by remember { mutableStateOf(false) }

    if (gameStarted && !retryFromGameOver) {
        GameScreen(
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