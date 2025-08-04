package com.sample.composeanimations

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sample.composeanimations.ui.screens.AnimationSamples
import com.sample.composeanimations.ui.theme.ComposeAnimationsTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(scrim = Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(scrim = Color.TRANSPARENT),
    )
    setContent {
      ComposeAnimationsTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AnimationSamples(innerPadding)
        }
      }
    }
  }
}
