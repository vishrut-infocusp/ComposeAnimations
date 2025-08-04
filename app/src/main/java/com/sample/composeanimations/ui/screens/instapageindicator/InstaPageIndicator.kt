package com.sample.composeanimations.ui.screens.instapageindicator

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.sample.composeanimations.ui.screens.AlphaAnimation
import com.sample.composeanimations.ui.screens.ColorAnimation
import com.sample.composeanimations.ui.screens.DpAnimation
import com.sample.composeanimations.ui.screens.PullToRefreshScreen
import com.sample.composeanimations.ui.screens.RotateAnimation
import com.sample.composeanimations.ui.screens.SizeAnimation
import com.sample.composeanimations.ui.screens.TranslationAnimation
import kotlin.math.max
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun InstaPageIndicator(modifier: Modifier = Modifier) {
  Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
    val pageCount = 7
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val indicatorScrollState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    var showOutline by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    var longPressOccurred by remember { mutableStateOf(false) }

    // For dragging
    var itemOffsetX by remember { mutableFloatStateOf(0f) }
    var itemOffsetY by remember { mutableFloatStateOf(0f) }

    val indicatorBounds = remember { mutableStateListOf<Rect?>() }
    LaunchedEffect(pageCount) {
      if (indicatorBounds.size != pageCount) {
        indicatorBounds.clear()
        repeat(pageCount) { indicatorBounds.add(null) }
      }
    }
    var draggedOverPageIndex by remember { mutableStateOf<Int?>(null) }

    val indicatorWidth = ((6 + 16) * 2 + 3 * (10 + 16)).dp // Hard computing it to simplify

    val density = LocalDensity.current

    LaunchedEffect(itemOffsetX, itemOffsetY) {
      with(density) { Log.d("OffsetChanged", "Item offset changed: ${itemOffsetX}, $itemOffsetY") }
    }

    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(
      key1 = pagerState.currentPage,
      block = {
        val currentPage = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
        val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

        if (currentPage > lastVisibleIndex - 1) {
          indicatorScrollState.animateScrollToItem(currentPage - size + 2)
        } else if (currentPage <= firstVisibleItemIndex + 1) {
          indicatorScrollState.animateScrollToItem(max(currentPage - 1, 0))
        }
      },
    )
    HorizontalPager(
      modifier = Modifier.weight(1f),
      state = pagerState,
      pageContent = { page ->
        when (page) {
          0 -> AlphaAnimation()
          1 -> TranslationAnimation()
          2 -> ColorAnimation()
          3 -> SizeAnimation()
          4 -> RotateAnimation()
          5 -> DpAnimation()
          else -> PullToRefreshScreen()
        }
      },
    )

    LazyRow(
      state = indicatorScrollState,
      modifier =
        Modifier.padding(vertical = 10.dp)
          .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
              onDragStart = { offset ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                var initialPage: Int? = null
                for (idx in 0 until pageCount) {
                  val bounds = indicatorBounds.getOrNull(idx)
                  if (bounds != null && offset.x >= bounds.left && offset.x <= bounds.right) {
                    initialPage = idx
                    break
                  }
                }

                if (initialPage != null) {
                  println("DRAG STARTED on LazyRow, initial page: $initialPage")
                  draggedOverPageIndex = initialPage
                  if (pagerState.currentPage != initialPage) {
                    scope.launch { pagerState.scrollToPage(initialPage) }
                  }
                }
                isDragging = true
                showOutline = true
              },
              onDrag = { change, dragAmount ->
                change.consume()
                val currentPointerX =
                  change.position.x / 1.25f // X-coordinate relative to the LazyRow
                var newPageIndex: Int? = null

                for (idx in 0 until pageCount) {
                  val bounds = indicatorBounds.getOrNull(idx)
                  if (
                    bounds != null &&
                    currentPointerX >= bounds.left &&
                    currentPointerX <= bounds.right
                  ) {
                    newPageIndex = idx
                    break
                  }
                }

                if (newPageIndex != null && newPageIndex != draggedOverPageIndex) {
                  draggedOverPageIndex = newPageIndex
                  println("Scrubbing over page: $newPageIndex")
                  if (pagerState.currentPage != newPageIndex) {
                    scope.launch {
                      hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                      pagerState.scrollToPage(newPageIndex)
                    }
                  }
                } else if (newPageIndex == null && draggedOverPageIndex != null) {
                  // Finger moved off any indicator, maybe clear selection or keep last
                  // For now, keep last draggedOverPageIndex
                }
              },
              onDragEnd = {
                if (
                  draggedOverPageIndex != null &&
                  pagerState.currentPage != draggedOverPageIndex
                ) {
                  val finalPage = draggedOverPageIndex!!
                  scope.launch { pagerState.animateScrollToPage(finalPage) }
                }
                isDragging = false
                showOutline = false // Reset overall indicator style
                draggedOverPageIndex = null
              },
              onDragCancel = {
                isDragging = false
                showOutline = false
                draggedOverPageIndex = null
              },
            )
          }
          .drawBehind {
            if (showOutline) {
              val radius = size.height / 2.0f
              drawRoundRect(
                cornerRadius = CornerRadius(x = radius, y = radius),
                color = Color(0xFF313131),
              )
            }
          }
          .height(24.dp)
          .width(indicatorWidth),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      repeat(pageCount) { iteration ->
        val color =
          if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
        item(key = "item$iteration") {
          val currentPage = pagerState.currentPage
          val firstVisibleIndex by remember {
            derivedStateOf { indicatorScrollState.firstVisibleItemIndex }
          }
          val lastVisibleIndex =
            indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
          val size by
            animateDpAsState(
              targetValue =
                if (iteration == currentPage) {
                  8.dp
                } else if (iteration in firstVisibleIndex + 1..lastVisibleIndex - 1) {
                  8.dp
                } else {
                  4.dp
                }
            )
          Box(
            modifier =
              Modifier.onGloballyPositioned { coordinates ->
                  if (iteration < indicatorBounds.size) {
                    indicatorBounds[iteration] = coordinates.boundsInParent()
                  }
                }
                .padding(horizontal = 4.dp)
                .pointerInput(Unit) {
                  detectTapGestures(
                    onPress = {
                      longPressOccurred = false
                      try {
                        awaitRelease() // Wait for the press to end (up or cancel)
                        // If it was not a long press that started a drag, ensure outline is false.
                        if (
                          !isDragging && !longPressOccurred
                        ) { // if not dragging AND no long press was registered by the other
                          // detector
                          if (showOutline) {
                            println(
                              "detectTapGestures: Resetting outline because interaction ended without drag/longpress."
                            )
                            showOutline = false
                          }
                        }
                        // isDragging will be reset by detectDragGesturesAfterLongPress's
                        // onDragEnd/Cancel
                      } catch (c: CancellationException) {
                        if (!isDragging) { // Only reset if a drag didn't take over
                          showOutline = false
                        }
                        // isDragging = false; // Reset dragging state
                        // longPressOccurred = false;
                        throw c
                      }
                    },
                    onTap = {
                      if (
                        !isDragging && !longPressOccurred
                      ) { // Ensure not in a drag/longpress initiated sequence
                        scope.launch {
                          hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                          pagerState.animateScrollToPage(iteration)
                        }
                        showOutline = false // Ensure outline is off for simple taps
                      }
                    },
                  )
                }
                .background(color, CircleShape)
                .size(size)
          )
        }
      }
    }
  }
}
