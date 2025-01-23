package com.takanakonbu.penguinsort.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.takanakonbu.penguinsort.ui.game.GameState
import com.takanakonbu.penguinsort.ui.theme.GothicFontFamily
import com.takanakonbu.penguinsort.ui.theme.PrimaryColor

@Composable
fun StartScreen(
    onStartClick: () -> Unit,
    scoreUpdateTrigger: Int // スコア更新のトリガーとして使用
) {
    // State to track which screen is shown
    val showHowToPlay = remember { mutableStateOf(false) }
    val showScores = remember { mutableStateOf(false) }
    val context = LocalContext.current
    // トリガーが変更されるたびにスコアを再読み込み
    val highScores = remember(scoreUpdateTrigger) {
        GameState.loadHighScores(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.start),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        when {
            showScores.value -> {
                // Scores screen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "ハイスコア",
                            color = Color.White,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        highScores.forEachIndexed { index, score ->
                            Text(
                                text = "${index + 1}位：${score}問",
                                color = Color.White,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Show remaining empty slots if less than 3 scores
                        repeat(3 - highScores.size) { index ->
                            Text(
                                text = "${highScores.size + index + 1}位：--",
                                color = Color.Gray,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { showScores.value = false },
                            modifier = Modifier
                                .width(200.dp)
                                .height(60.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor
                            )
                        ) {
                            Text(
                                text = "戻る",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
            showHowToPlay.value -> {
                // How to Play screen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "遊び方",
                            color = Color.White,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "画面に表示されたペンギンの配置を\n3秒間記憶してください。\n\n" +
                                    "その後、同じ順番にペンギンをタップして\n" +
                                    "正確に並べましょう。\n\n" +
                                    "間違えるとゲームオーバーです！",
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Button(
                            onClick = { showHowToPlay.value = false },
                            modifier = Modifier
                                .width(200.dp)
                                .height(60.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor
                            )
                        ) {
                            Text(
                                text = "戻る",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
            else -> {
                // Main menu screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ペンギン隊列",
                        fontSize = 40.sp,
                        color = PrimaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontFamily = GothicFontFamily
                    )
                    Text(
                        text = "配置を覚えてペンギンを整列させよう！",
                        fontSize = 24.sp,
                        color = PrimaryColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontFamily = GothicFontFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Three buttons in a Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row {
                            Button(
                                onClick = onStartClick,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(60.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text(
                                    text = "スタート",
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { showHowToPlay.value = true },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(60.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text(
                                    text = "遊び方",
                                    fontSize = 20.sp
                                )
                            }
                        }

                        Button(
                            onClick = { showScores.value = true },
                            modifier = Modifier
                                .width(200.dp)
                                .height(60.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor
                            )
                        ) {
                            Text(
                                text = "スコア",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}