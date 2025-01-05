package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.takanakonbu.penguinsort.R
import com.takanakonbu.penguinsort.model.PenguinType
import com.takanakonbu.penguinsort.ui.game.GamePhase
import com.takanakonbu.penguinsort.ui.game.GameState
import com.takanakonbu.penguinsort.ui.theme.PrimaryColor
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    onRetry: () -> Unit = {}
) {
    val context = LocalContext.current
    var gameState by remember { mutableStateOf(GameState()) }
    var currentPenguinCount by remember { mutableStateOf(3) }

    // 問題数に応じてペンギンの数を決定する関数
    fun getPenguinCount(solvedProblems: Int): Int {
        return when {
            solvedProblems >= 12 -> 10  // 3 + 4 + 5問で12問以上
            solvedProblems >= 7 -> 7    // 3 + 4問で7問以上
            solvedProblems >= 3 -> 5    // 3問以上
            else -> 3                    // 開始時
        }
    }

    // ゲーム開始時の初期化
    fun initializeGame() {
        currentPenguinCount = getPenguinCount(gameState.solvedProblems)
        val randomPenguins = PenguinType.entries.shuffled().take(currentPenguinCount)
        gameState = gameState.copy(
            targetPenguins = randomPenguins,
            shuffledPenguins = randomPenguins.shuffled(),
            selectedPenguins = emptyList(),
            gamePhase = GamePhase.SHOWING
        )
    }

    // 初回のゲーム開始
    LaunchedEffect(Unit) {
        initializeGame()
        delay(3000)
        gameState = gameState.copy(gamePhase = GamePhase.PLAYING)
    }

    // 3秒表示後にプレイフェーズへ移行
    LaunchedEffect(gameState.gamePhase) {
        if (gameState.gamePhase == GamePhase.SHOWING && !gameState.isGameOver) {
            delay(3000)
            gameState = gameState.copy(gamePhase = GamePhase.PLAYING)
        }
    }

    // ペンギンをタップしたときの処理
    fun onPenguinClick(penguin: PenguinType) {
        if (gameState.gamePhase != GamePhase.PLAYING) return

        val currentIndex = gameState.selectedPenguins.size
        val targetPenguin = gameState.targetPenguins.getOrNull(currentIndex)

        if (penguin == targetPenguin) {
            // 正解の場合
            val newSelectedPenguins = gameState.selectedPenguins + penguin
            gameState = gameState.copy(selectedPenguins = newSelectedPenguins)

            // すべて選択完了した場合
            if (newSelectedPenguins.size == gameState.targetPenguins.size) {
                // 問題数をインクリメント
                val newSolvedProblems = gameState.solvedProblems + 1
                gameState = gameState.copy(solvedProblems = newSolvedProblems)

                // 新しいペンギン数を取得
                val newPenguinCount = getPenguinCount(newSolvedProblems)
                if (newPenguinCount != currentPenguinCount) {
                    currentPenguinCount = newPenguinCount
                }

                // 新しいラウンドを開始
                initializeGame()
            }
        } else {
            // 不正解の場合
            gameState = gameState.copy(
                gamePhase = GamePhase.GAME_OVER,
                isGameOver = true
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景画像
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // 解いた問題数の表示
        Text(
            text = "解いた問題: ${gameState.solvedProblems}問",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // 上部の目標配置表示
        if (gameState.gamePhase == GamePhase.SHOWING) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                gameState.targetPenguins.forEach { penguin ->
                    Image(
                        painter = painterResource(id = penguin.getResourceId(context)),
                        contentDescription = "Penguin ${penguin.name}",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        // プレイ画面
        if (gameState.gamePhase == GamePhase.PLAYING) {
            // 選択済みペンギン（氷の上）
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                gameState.selectedPenguins.forEach { penguin ->
                    Image(
                        painter = painterResource(id = penguin.getResourceId(context)),
                        contentDescription = "Selected ${penguin.name}",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // 選択可能なペンギン（中央）
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                gameState.shuffledPenguins.forEach { penguin ->
                    val isSelected = penguin in gameState.selectedPenguins
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isSelected) {
                            Image(
                                painter = painterResource(id = penguin.getResourceId(context)),
                                contentDescription = "Penguin ${penguin.name}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onPenguinClick(penguin) }
                            )
                        }
                    }
                }
            }
        }

        // ゲームオーバー表示
        if (gameState.gamePhase == GamePhase.GAME_OVER) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(
                        text = "ゲームオーバー",
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "クリアした問題数: ${gameState.solvedProblems}問",
                        color = Color.White,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { onRetry() },
                        modifier = Modifier
                            .width(200.dp)
                            .height(60.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        )
                    ) {
                        Text(
                            text = "リトライ",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}