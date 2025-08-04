package com.sample.composeanimations.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ColorAnimation() {
  var tapped: Boolean by remember { mutableStateOf(false) }
  val animatedTextColor: Color by
    animateColorAsState(
      if (tapped) Color.Cyan else Color.Magenta,
      label = "color",
      animationSpec = tween(durationMillis = 3_000),
    )

  Scaffold(topBar = { TopAppBar(title = { Text("Color Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding()).clickable {
        tapped = !tapped
      },
      contentAlignment = Alignment.CenterStart,
    ) {
      Text(
        text = "Hello Compose",
        color = animatedTextColor,
        fontSize = 72.sp,
        lineHeight = 80.sp,
        style = MaterialTheme.typography.headlineLarge,
      )
    }
  }
}
