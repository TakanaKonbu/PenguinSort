package com.takanakonbu.penguinsort.ui.game

import android.content.Context
import android.content.SharedPreferences
import com.takanakonbu.penguinsort.model.PenguinType

/**
 * ゲームの状態を管理するデータクラス
 */
data class GameState(
    val targetPenguins: List<PenguinType> = emptyList(),
    val shuffledPenguins: List<PenguinType> = emptyList(),
    val selectedPenguins: List<PenguinType> = emptyList(),
    val gamePhase: GamePhase = GamePhase.SHOWING,
    val isGameOver: Boolean = false,
    val solvedProblems: Int = 0,
    val currentLevel: Int = 1,
    val highScores: List<Int> = emptyList()
) {
    companion object {
        private const val PREFS_NAME = "PenguinSortPrefs"
        private const val SCORE_1_KEY = "highScore1"
        private const val SCORE_2_KEY = "highScore2"
        private const val SCORE_3_KEY = "highScore3"

        // レベルごとのペンギンの数
        const val LEVEL_1_PENGUINS = 3  // 初期レベル（3匹）
        const val LEVEL_2_PENGUINS = 5  // 3問クリア後（5匹）
        const val LEVEL_3_PENGUINS = 7  // 7問クリア後（7匹）
        const val LEVEL_4_PENGUINS = 10 // 12問クリア後（10匹）

        // レベルアップに必要な問題数
        const val LEVEL_2_THRESHOLD = 3  // レベル2へのしきい値
        const val LEVEL_3_THRESHOLD = 7  // レベル3へのしきい値
        const val LEVEL_4_THRESHOLD = 12 // レベル4へのしきい値

        /**
         * 保存されているハイスコアを読み込む
         */
        fun loadHighScores(context: Context): List<Int> {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return listOf(
                prefs.getInt(SCORE_1_KEY, 0),
                prefs.getInt(SCORE_2_KEY, 0),
                prefs.getInt(SCORE_3_KEY, 0)
            ).filter { it > 0 }.sorted().reversed()
        }

        /**
         * 新しいスコアを保存し、TOP3を更新する
         */
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

    /**
     * 現在のレベルに応じたペンギンの数を返す
     */
    fun getPenguinCountForLevel(): Int {
        return when (currentLevel) {
            1 -> LEVEL_1_PENGUINS
            2 -> LEVEL_2_PENGUINS
            3 -> LEVEL_3_PENGUINS
            4 -> LEVEL_4_PENGUINS
            else -> LEVEL_1_PENGUINS
        }
    }

    /**
     * 問題数に応じた適切なレベルを計算する
     */
    fun calculateLevel(problems: Int): Int {
        return when {
            problems >= LEVEL_4_THRESHOLD -> 4
            problems >= LEVEL_3_THRESHOLD -> 3
            problems >= LEVEL_2_THRESHOLD -> 2
            else -> 1
        }
    }
}

/**
 * ゲームの進行フェーズを表す列挙型
 */
enum class GamePhase {
    SHOWING,     // 3秒間表示の確認フェーズ
    PLAYING,     // プレイ中のフェーズ
    GAME_OVER    // ゲームオーバーフェーズ
}