package com.takanakonbu.penguinsort.ui.game

import android.content.Context
import com.takanakonbu.penguinsort.model.PenguinType

data class GameState(
    val targetPenguins: List<PenguinType> = emptyList(),
    val shuffledPenguins: List<PenguinType> = emptyList(),
    val selectedPenguins: List<PenguinType> = emptyList(),
    val gamePhase: GamePhase = GamePhase.SHOWING,
    val isGameOver: Boolean = false,
    val solvedProblems: Int = 0,
    val currentLevel: Int = 1,
    val highScores: List<Int> = emptyList(),
    val remainingTime: Float = 1f  // 1f = 100%, 0f = 0%
) {
    companion object {
        private const val PREFS_NAME = "PenguinSortPrefs"
        private const val SCORE_1_KEY = "highScore1"
        private const val SCORE_2_KEY = "highScore2"
        private const val SCORE_3_KEY = "highScore3"

        const val MAX_TIME = 5000L // 5秒
        const val TIME_UPDATE_INTERVAL = 16L // 約60FPS

        fun loadHighScores(context: Context): List<Int> {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return listOf(
                prefs.getInt(SCORE_1_KEY, 0),
                prefs.getInt(SCORE_2_KEY, 0),
                prefs.getInt(SCORE_3_KEY, 0)
            ).filter { it > 0 }.sorted().reversed()
        }

        fun saveNewScore(context: Context, newScore: Int) {
            val currentScores = loadHighScores(context).toMutableList()
            currentScores.add(newScore)
            val top3Scores = currentScores.sorted().reversed().take(3)

            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()

            editor.apply {
                putInt(SCORE_1_KEY, top3Scores.getOrElse(0) { 0 })
                putInt(SCORE_2_KEY, top3Scores.getOrElse(1) { 0 })
                putInt(SCORE_3_KEY, top3Scores.getOrElse(2) { 0 })
                apply()
            }
        }
    }
}

enum class GamePhase {
    SHOWING,
    PLAYING,
    GAME_OVER
}