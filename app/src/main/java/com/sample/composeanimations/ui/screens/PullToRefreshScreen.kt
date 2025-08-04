package com.sample.composeanimations.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sample.composeanimations.models.RatingData
import com.sample.composeanimations.ui.components.RatingCard
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val ratingsList =
  listOf(
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
    RatingData(
      userName = "EchoesInMySoul",
      rating = "5.0",
      title = "Mind-Blowing Performance",
      description =
        "Pink Floyd's performance was beyond my expectations. The way they seamlessly transitioned between songs and the incredible guitar solos left the audience in awe. Definitely a night to remember!",
    ),
  )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshScreen(modifier: Modifier = Modifier) {
  val scope = rememberCoroutineScope()

  var isRefreshing by remember { mutableStateOf(false) }
  val pullRefreshState = rememberPullToRefreshState()
  val onRefresh: () -> Unit = {
    isRefreshing = true
    scope.launch {
      delay(5_00)
      isRefreshing = false
    }
  }

  val willRefresh by remember { derivedStateOf { pullRefreshState.distanceFraction > 1f } }

  val hapticFeedback = LocalHapticFeedback.current
  LaunchedEffect(willRefresh) {
    when {
      willRefresh -> {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        delay(70)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        delay(100)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
      }

      !isRefreshing && pullRefreshState.distanceFraction > 0f -> {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
      }
    }
  }

  val cardOffsetValue = 250

  val cardOffset by
    animateIntAsState(
      targetValue =
        when {
          isRefreshing -> cardOffsetValue
          pullRefreshState.distanceFraction in 0f..1f ->
            (cardOffsetValue * pullRefreshState.distanceFraction).roundToInt()
          pullRefreshState.distanceFraction > 1f ->
            (cardOffsetValue + ((pullRefreshState.distanceFraction - 1f) * .1f) * 100).roundToInt()
          else -> 0
        },
      label = "cardOffset",
      animationSpec =
        spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
    )

  val rotationDegree = 5f
  val cardRotation by
    animateFloatAsState(
      targetValue =
        when {
          isRefreshing || pullRefreshState.distanceFraction > 1f -> rotationDegree
          pullRefreshState.distanceFraction > 0f ->
            rotationDegree * pullRefreshState.distanceFraction
          else -> 0f
        },
      label = "cardRotation",
    )

  Scaffold(topBar = { TopAppBar(title = { Text("Pull to Refresh") }) }) { innerPadding ->
      Box(
        modifier =
          modifier.then(
            Modifier.pullToRefresh(
              enabled = true,
              isRefreshing = isRefreshing,
              state = pullRefreshState,
              threshold = PullToRefreshDefaults.PositionalThreshold,
              onRefresh = onRefresh,
            )
          )
      ) {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding =
            PaddingValues(
              top = innerPadding.calculateTopPadding() + 16.dp,
              bottom = innerPadding.calculateBottomPadding() + 16.dp,
              start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
              end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
            ),
        ) {
          itemsIndexed(items = ratingsList) { index, item ->
            val zigZagRotationModifier =
              Modifier
                .zIndex((/*ratingsList.size - */ index).toFloat())
                .graphicsLayer {
                  rotationZ = cardRotation * if (index % 2 == 0) 1 else -1
                  translationY =
                    (cardOffset * ((rotationDegree - (index + 1)) / rotationDegree))
                      .dp
                      .roundToPx()
                      .toFloat()
                }

            //        val xAxisRotationModifier =
            //          Modifier.zIndex(1.toFloat()).graphicsLayer {
            //            rotationX = cardRotation * if (index % 2 == 0) 1 else -1
            //            translationY =
            //              ((cardOffset / 1.5f) * ((rotationDegree - (index + 1)) / rotationDegree))
            //                .dp
            //                .roundToPx()
            //                .toFloat()
            //          }

            //        val yAxisRotationModifier =
            //          Modifier.zIndex(1.toFloat()).graphicsLayer {
            //            rotationY = cardRotation * if (index % 2 == 0) 1 else -1
            //            translationY =
            //              ((cardOffset / 1.5f) * ((rotationDegree - (index + 1)) / rotationDegree))
            //                .dp
            //                .roundToPx()
            //                .toFloat()
            //          }

            RatingCard(modifier = zigZagRotationModifier, item)
          }
        }

        Indicator(
          isRefreshing = isRefreshing,
          state = pullRefreshState,
          modifier =
            Modifier
              .align(Alignment.TopCenter)
              .padding(top = innerPadding.calculateTopPadding()),
        )
      }
    }
}
