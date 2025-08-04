package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TranslationAnimation() {
  var offset: Offset by remember { mutableStateOf(Offset(0f, 0f)) }
  val animatedOffset: Offset by animateOffsetAsState(offset, label = "offset")

  Scaffold(topBar = { TopAppBar(title = { Text("Offset Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding()).pointerInput(Unit) {
        detectTapGestures(onTap = { offsetValue -> offset = offsetValue })
      }
    ) {
      Box(
        Modifier.size(100.dp)
          .graphicsLayer {
            translationX = animatedOffset.x
            translationY = animatedOffset.y
          }
          .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
      )
    }
  }
}
