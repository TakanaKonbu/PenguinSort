package com.takanakonbu.penguinsort

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.takanakonbu.penguinsort.ui.theme.PenguinSortTheme
import com.takanakonbu.penguinsort.ui.screen.MainScreen
import com.takanakonbu.penguinsort.ui.components.AdManager
import com.google.android.gms.ads.MobileAds
import com.takanakonbu.penguinsort.sound.SoundManager

class MainActivity : ComponentActivity() {
    private lateinit var adManager: AdManager
    private lateinit var soundManager: SoundManager

    override fun onStart() {
        super.onStart()
        // アプリが表示されたときにBGMを開始
        soundManager.startBgm()
    }

    override fun onStop() {
        super.onStop()
        // アプリがバックグラウンドに移動したときにBGMを停止
        soundManager.stopBgm()
    }

    override fun onDestroy() {
        super.onDestroy()
        // アプリが終了するときにサウンドリソースを解放
        soundManager.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize managers
        MobileAds.initialize(this)
        soundManager = SoundManager(this)
        adManager = AdManager(this, soundManager)

        // エッジトゥエッジモードを有効化
        enableEdgeToEdge()

        // ウィンドウをフルスクリーンに設定
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // ステータスバーとナビゲーションバーを非表示
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // 画面をONのままにする（オプション）
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            PenguinSortTheme {
                MainScreen(adManager = adManager, soundManager = soundManager)
            }
        }
    }
}