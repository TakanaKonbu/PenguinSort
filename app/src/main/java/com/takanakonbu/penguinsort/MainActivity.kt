package com.takanakonbu.penguinsort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.takanakonbu.penguinsort.ui.theme.PenguinSortTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PenguinSortTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var gameStarted by remember { mutableStateOf(false) }

    if (gameStarted) {
        GameScreen()
    } else {
        StartScreen(onStartClick = { gameStarted = true })
    }
}

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 背景画像
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // タイトルとスタートボタン
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "配置を覚えてペンギンを整列させよう！",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = "スタート",
                    fontSize = 20.sp
                )
            }
        }
    }
}

// ペンギンの種類を定義
enum class PenguinType(val resourceName: String) {
    NORMAL("normal"),
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    ORANGE("orange"),
    PURPLE("purple"),
    GRAY("gray"),
    SKY("sky"),
    LIGHTGREEN("lightgreen"),
    PINK("pink");

    fun getResourceId(context: android.content.Context): Int {
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
    }
}

@Composable
fun GameScreen() {
    var penguins by remember { mutableStateOf(emptyList<PenguinType>()) }
    var isVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // ゲーム開始時に異なるペンギンをランダムに選択
    LaunchedEffect(Unit) {
        val randomPenguins = PenguinType.values().toList().shuffled().take(3)
        penguins = randomPenguins
        isVisible = true
        delay(3000) // 3秒間表示
        isVisible = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景画像
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // 上部の半透明の黒背景とペンギン
        if (isVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                penguins.forEach { penguin ->
                    Image(
                        painter = painterResource(id = penguin.getResourceId(context)),
                        contentDescription = "Penguin ${penguin.name}",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }
    }
}