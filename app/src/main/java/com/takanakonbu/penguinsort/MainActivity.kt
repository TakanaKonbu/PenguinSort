package com.takanakonbu.penguinsort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.takanakonbu.penguinsort.ui.theme.PenguinSortTheme
import com.takanakonbu.penguinsort.ui.screen.MainScreen

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