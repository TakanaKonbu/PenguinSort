package com.takanakonbu.penguinsort.ui.game

import com.takanakonbu.penguinsort.model.PenguinType

/**
 * ゲームの状態を管理するデータクラス
 *
 * @property targetPenguins 目標のペンギン配列（お手本）
 * @property shuffledPenguins シャッフルされたペンギン配列（選択肢）
 * @property selectedPenguins プレイヤーが選択したペンギンの配列
 * @property gamePhase 現在のゲームフェーズ
 * @property isGameOver ゲームオーバーフラグ
 * @property solvedProblems 解いた問題数
 * @property currentLevel 現在の難易度レベル（3匹=1, 5匹=2, 7匹=3, 10匹=4）
 */
data class GameState(
    val targetPenguins: List<PenguinType> = emptyList(),
    val shuffledPenguins: List<PenguinType> = emptyList(),
    val selectedPenguins: List<PenguinType> = emptyList(),
    val gamePhase: GamePhase = GamePhase.SHOWING,
    val isGameOver: Boolean = false,
    val solvedProblems: Int = 0,
    val currentLevel: Int = 1
) {
    companion object {
        // レベルごとのペンギンの数
        const val LEVEL_1_PENGUINS = 3  // 初期レベル（3匹）
        const val LEVEL_2_PENGUINS = 5  // 3問クリア後（5匹）
        const val LEVEL_3_PENGUINS = 7  // 7問クリア後（7匹）
        const val LEVEL_4_PENGUINS = 10 // 12問クリア後（10匹）

        // レベルアップに必要な問題数
        const val LEVEL_2_THRESHOLD = 3  // レベル2へのしきい値
        const val LEVEL_3_THRESHOLD = 7  // レベル3へのしきい値
        const val LEVEL_4_THRESHOLD = 12 // レベル4へのしきい値
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