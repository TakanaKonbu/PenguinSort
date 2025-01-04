package com.takanakonbu.penguinsort.ui.game

import com.takanakonbu.penguinsort.model.PenguinType

data class GameState(
    val targetPenguins: List<PenguinType> = emptyList(),
    val shuffledPenguins: List<PenguinType> = emptyList(),
    val selectedPenguins: List<PenguinType> = emptyList(),
    val gamePhase: GamePhase = GamePhase.SHOWING,
    val isGameOver: Boolean = false
)

enum class GamePhase {
    SHOWING,     // 3秒間表示
    PLAYING,     // プレイ中
    GAME_OVER    // ゲームオーバー
}