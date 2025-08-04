package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RotateAnimation() {
  var rotation: Int by remember { mutableIntStateOf(0) }
  val animatedAngle: Int by
    animateIntAsState(rotation, label = "rotate", animationSpec = tween(durationMillis = 1_000))

  Scaffold(topBar = { TopAppBar(title = { Text("Rotate Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding()).clickable {
        rotation += 90
      },
      contentAlignment = Alignment.Center,
    ) {
      Box(
        Modifier.size(100.dp)
          .graphicsLayer { rotationZ = animatedAngle.toFloat() }
          .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
      )
    }
  }
}
