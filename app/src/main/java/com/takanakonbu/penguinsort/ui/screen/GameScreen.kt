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
    var currentPenguinCount by remember { mutableIntStateOf(3) }

    // ゲームロジック関数（変更なし）
    fun getPenguinCount(solvedProblems: Int): Int {
        return when {
            solvedProblems >= 15 -> 10
            solvedProblems >= 10 -> 7
            solvedProblems >= 5 -> 5
            else -> 3
        }
    }

    fun initializeGame() {
        currentPenguinCount = getPenguinCount(gameState.solvedProblems)
        val randomPenguins = PenguinType.entries.shuffled().take(currentPenguinCount)
        gameState = gameState.copy(
            targetPenguins = randomPenguins,
            shuffledPenguins = randomPenguins.shuffled(),
            selectedPenguins = emptyList(),
            gamePhase = GamePhase.SHOWING,
            remainingTime = 1f
        )
    }

    fun onPenguinClick(penguin: PenguinType) {
        if (gameState.gamePhase != GamePhase.PLAYING) return

        val currentIndex = gameState.selectedPenguins.size
        val targetPenguin = gameState.targetPenguins.getOrNull(currentIndex)

        if (penguin == targetPenguin) {
            val newSelectedPenguins = gameState.selectedPenguins + penguin
            gameState = gameState.copy(selectedPenguins = newSelectedPenguins)

            if (newSelectedPenguins.size == gameState.targetPenguins.size) {
                val newSolvedProblems = gameState.solvedProblems + 1
                gameState = gameState.copy(solvedProblems = newSolvedProblems)

                val newPenguinCount = getPenguinCount(newSolvedProblems)
                if (newPenguinCount != currentPenguinCount) {
                    currentPenguinCount = newPenguinCount
                }

                initializeGame()
            }
        } else {
            GameState.saveNewScore(context, gameState.solvedProblems)
            gameState = gameState.copy(
                gamePhase = GamePhase.GAME_OVER,
                isGameOver = true,
                highScores = GameState.loadHighScores(context)
            )
        }
    }

    // LaunchedEffectブロック（変更なし）
    LaunchedEffect(Unit) {
        initializeGame()
        delay(3000)
        gameState = gameState.copy(gamePhase = GamePhase.PLAYING)
    }

    LaunchedEffect(gameState.gamePhase) {
        if (gameState.gamePhase == GamePhase.PLAYING && !gameState.isGameOver) {
            var remainingTime = GameState.MAX_TIME
            while (remainingTime > 0) {
                delay(GameState.TIME_UPDATE_INTERVAL)
                remainingTime -= GameState.TIME_UPDATE_INTERVAL
                gameState = gameState.copy(
                    remainingTime = remainingTime.toFloat() / GameState.MAX_TIME
                )
            }
            GameState.saveNewScore(context, gameState.solvedProblems)
            gameState = gameState.copy(
                gamePhase = GamePhase.GAME_OVER,
                isGameOver = true,
                highScores = GameState.loadHighScores(context)
            )
        } else if (gameState.gamePhase == GamePhase.SHOWING && !gameState.isGameOver) {
            delay(3000)
            gameState = gameState.copy(
                gamePhase = GamePhase.PLAYING,
                remainingTime = 1f
            )
        }
    }

    // メインのUI構造
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 上部エリア
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                // スコア表示
                Text(
                    text = "解いた問題: ${gameState.solvedProblems}問",
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                )

                if (gameState.gamePhase == GamePhase.PLAYING) {
                    // タイムバー
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                            .height(8.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(gameState.remainingTime)
                                .background(PrimaryColor)
                        )
                    }
                } else if (gameState.gamePhase == GamePhase.SHOWING) {
                    // 目標配置表示
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        gameState.targetPenguins.forEach { penguin ->
                            Image(
                                painter = painterResource(id = penguin.getResourceId(context)),
                                contentDescription = "Target Penguin ${penguin.name}",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }
            }

            // 中央エリア（選択肢ペンギン）
            if (gameState.gamePhase == GamePhase.PLAYING) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
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
                                        contentDescription = "Choice Penguin ${penguin.name}",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable { onPenguinClick(penguin) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 下部エリア（氷山とペンギン）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                // 氷山の画像
                Image(
                    painter = painterResource(id = R.drawable.bgclear),
                    contentDescription = "Ice Platform",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.FillWidth
                )

                // 選択済みペンギン
                if (gameState.gamePhase == GamePhase.PLAYING) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 80.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        gameState.selectedPenguins.forEach { penguin ->
                            Image(
                                painter = painterResource(id = penguin.getResourceId(context)),
                                contentDescription = "Selected Penguin ${penguin.name}",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }
            }
        }

        // ゲームオーバー表示（オーバーレイ）
        if (gameState.gamePhase == GamePhase.GAME_OVER) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "ゲームオーバー",
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "今回のスコア：${gameState.solvedProblems}問",
                        color = Color.White,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ハイスコア表示
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ハイスコア",
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        gameState.highScores.forEachIndexed { index, score ->
                            Text(
                                text = "${index + 1}位：${score}問",
                                color = if (gameState.solvedProblems == score) Color.Yellow else Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        repeat(3 - gameState.highScores.size) { index ->
                            Text(
                                text = "${gameState.highScores.size + index + 1}位：--",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onRetry() },
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
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