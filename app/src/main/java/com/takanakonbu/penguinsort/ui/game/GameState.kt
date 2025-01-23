package com.takanakonbu.penguinsort.ui.game

import android.content.Context
import com.takanakonbu.penguinsort.model.PenguinType
import android.util.Log

data class GameState(
    val targetPenguins: List<PenguinType> = emptyList(),
    val shuffledPenguins: List<PenguinType> = emptyList(),
    val selectedPenguins: List<PenguinType> = emptyList(),
    val gamePhase: GamePhase = GamePhase.SHOWING,
    val isGameOver: Boolean = false,
    val solvedProblems: Int = 0,
    val currentLevel: Int = 1,
    val highScores: List<Int> = emptyList(),
    val remainingTime: Float = 1f,  // 1f = 100%, 0f = 0%
    val continuedCount: Int = 0,    // コンティニュー回数
    val isContinueAvailable: Boolean = true  // コンティニュー可能かどうかのフラグ
) {
    companion object {
        private const val TAG = "GameState"
        private const val PREFS_NAME = "PenguinSortPrefs"
        private const val SCORE_1_KEY = "highScore1"
        private const val SCORE_2_KEY = "highScore2"
        private const val SCORE_3_KEY = "highScore3"
        private const val MAX_CONTINUE_COUNT = 1

        const val MAX_TIME = 5000L // 5秒
        const val TIME_UPDATE_INTERVAL = 16L // 約60FPS

        fun loadHighScores(context: Context): List<Int> {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val scores = listOf(
                prefs.getInt(SCORE_1_KEY, 0),
                prefs.getInt(SCORE_2_KEY, 0),
                prefs.getInt(SCORE_3_KEY, 0)
            ).filter { it > 0 }.sorted().reversed()

            Log.d(TAG, "Loaded scores: $scores")
            return scores
        }

        fun saveNewScore(context: Context, newScore: Int) {
            Log.d(TAG, "Attempting to save new score: $newScore")

            val currentScores = loadHighScores(context).toMutableList()
            Log.d(TAG, "Current scores before adding new score: $currentScores")

            // 新しいスコアを追加
            currentScores.add(newScore)

            // スコアを降順にソート
            val sortedScores = currentScores.sortedDescending()
            Log.d(TAG, "Sorted scores: $sortedScores")

            // 上位3つのスコアを取得
            val top3Scores = sortedScores.take(3)
            Log.d(TAG, "Top 3 scores to save: $top3Scores")

            // スコアを保存
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().apply {
                putInt(SCORE_1_KEY, top3Scores.getOrElse(0) { 0 })
                putInt(SCORE_2_KEY, top3Scores.getOrElse(1) { 0 })
                putInt(SCORE_3_KEY, top3Scores.getOrElse(2) { 0 })
                apply()
            }

            // 保存後の確認
            val savedScores = loadHighScores(context)
            Log.d(TAG, "Scores after saving: $savedScores")
        }
    }

    fun canContinue(): Boolean {
        return isContinueAvailable && continuedCount < MAX_CONTINUE_COUNT
    }

    fun getRemainingContinues(): Int {
        return MAX_CONTINUE_COUNT - continuedCount
    }
}

enum class GamePhase {
    SHOWING,
    PLAYING,
    GAME_OVER
}