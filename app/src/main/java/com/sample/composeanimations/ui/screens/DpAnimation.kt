package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DpAnimation() {
  var initialHeight: Dp = 100.dp
  var heightInDp: Dp by remember { mutableStateOf(initialHeight) }
  val animatedHeightInDp: Dp by
    animateDpAsState(
      heightInDp,
      label = "heightInDp",
      animationSpec = tween(durationMillis = 1_000),
    )

  Scaffold(topBar = { TopAppBar(title = { Text("Dp Animation") }) }) { innerPadding ->
    Box(
      Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding()).clickable {
        heightInDp = if (heightInDp == initialHeight) 200.dp else initialHeight
      }
    ) {
      Box(
        Modifier.padding(16.dp)
          .fillMaxWidth()
          .height(animatedHeightInDp)
          .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
      )
    }
  }
}
