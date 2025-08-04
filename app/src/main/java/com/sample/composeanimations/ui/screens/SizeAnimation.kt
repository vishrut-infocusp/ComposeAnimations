package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateSizeAsState
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SizeAnimation() {
  var initialSize = 300f
  var size: Float by remember { mutableFloatStateOf(initialSize) }
  val animatedSize: Size by animateSizeAsState(Size(width = size, height = size), label = "size")

  val density = LocalDensity.current

  Scaffold(topBar = { TopAppBar(title = { Text("Size Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding()).clickable {
        size = if (size == initialSize) 700f else initialSize
      }
    ) {
      Box(
        Modifier.size(size = with(density) { animatedSize.toDpSize() })
          .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
      )
    }
  }
}
