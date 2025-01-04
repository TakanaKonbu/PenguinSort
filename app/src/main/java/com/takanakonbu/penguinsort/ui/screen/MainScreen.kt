package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.runtime.*

@Composable
fun MainScreen() {
    var gameStarted by remember { mutableStateOf(false) }

    if (gameStarted) {
        GameScreen()
    } else {
        StartScreen(onStartClick = { gameStarted = true })
    }
}