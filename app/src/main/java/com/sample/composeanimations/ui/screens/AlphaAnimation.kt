package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AlphaAnimation() {
  var tapped: Boolean by remember { mutableStateOf(false) }
  val animatedAlpha: Float by
    animateFloatAsState(
      if (tapped) 1f else 0.5f,
      label = "alpha",
      animationSpec = tween(durationMillis = if (tapped) 0 else 2_000),
      finishedListener = { tapped = false },
    )

  Scaffold(topBar = { TopAppBar(title = { Text("Alpha Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize()
        .padding(top = innerPadding.calculateTopPadding())
        .clickable { tapped = true }
        .graphicsLayer {
          alpha = animatedAlpha
        } // Redraw underlying layer
        .background(Color.Red)
    )
  }
}
