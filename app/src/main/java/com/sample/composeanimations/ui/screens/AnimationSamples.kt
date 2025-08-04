package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AnimationSamples(innerPadding: PaddingValues) {
  val pageCount = 7
  val pagerState = rememberPagerState { pageCount }
  var currentPage by remember { mutableIntStateOf(0) }

  val scope = rememberCoroutineScope()

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { currentPage = it }
  }

  //  InstaPageIndicator(modifier = Modifier.padding(bottom =
  // innerPadding.calculateBottomPadding()))

  Column(
    modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
  ) {
    HorizontalPager(
      modifier = Modifier.weight(1f),
      state = pagerState,
      pageContent = { page ->
        Box(modifier = Modifier) {
          when (page) {
            0 -> AlphaAnimation()
            1 -> TranslationAnimation()
            2 -> ColorAnimation()
            3 -> SizeAnimation()
            4 -> RotateAnimation()
            5 -> DpAnimation()
            else -> PullToRefreshScreen()
          }
        }
      },
    )

    Row(
      Modifier.height(30.dp).fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
      repeat(pageCount) { iteration ->
        val color =
          if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)

        val indicatorWidth by
          animateDpAsState(
            targetValue =
              if (iteration == currentPage) {
                36.dp
              } else {
                8.dp
              },
            animationSpec = tween(durationMillis = 400),
          )

        Box(
          modifier =
            Modifier.clickable { scope.launch { pagerState.animateScrollToPage(iteration) } }
              .clip(CircleShape)
              .background(color)
              .width(indicatorWidth)
              .height(8.dp)
        )
      }
    }
  }
}
