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

        // レベルごとのペンギンの数
        const val LEVEL_1_PENGUINS = 3
        const val LEVEL_2_PENGUINS = 5
        const val LEVEL_3_PENGUINS = 7
        const val LEVEL_4_PENGUINS = 10

        // レベルアップに必要な問題数
        const val LEVEL_2_THRESHOLD = 3
        const val LEVEL_3_THRESHOLD = 7
        const val LEVEL_4_THRESHOLD = 12

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

    fun getPenguinCountForLevel(): Int {
        return when (currentLevel) {
            1 -> LEVEL_1_PENGUINS
            2 -> LEVEL_2_PENGUINS
            3 -> LEVEL_3_PENGUINS
            4 -> LEVEL_4_PENGUINS
            else -> LEVEL_1_PENGUINS
        }
    }

    fun calculateLevel(problems: Int): Int {
        return when {
            problems >= LEVEL_4_THRESHOLD -> 4
            problems >= LEVEL_3_THRESHOLD -> 3
            problems >= LEVEL_2_THRESHOLD -> 2
            else -> 1
        }
    }
}

enum class GamePhase {
    SHOWING,
    PLAYING,
    GAME_OVER
}