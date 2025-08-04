package com.sample.composeanimations.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.math.absoluteValue
import kotlin.math.sqrt

data class ImageData(val title: String, val desc: String, val bitmap: Bitmap)

@Composable
fun CircularRevealSample(innerPadding: PaddingValues) {
  val ctx = LocalContext.current

  val imageDetailsList: List<Pair<String, String>> =
    listOf(
      Pair(
        "Giethoorn's Tranquil Waterways",
        "A serene canal winds through a picturesque village, reflecting the clear sky above. Charming thatched-roof cottages with blue and green shutters are nestled amidst lush greenery and vibrant flowers.",
      ),
      Pair(
        "Dubai's Iconic Ascent",
        "The majestic Burj Khalifa, a gleaming spire of modern architecture, soars into a partially clouded sky.\n" +
          "Its reflective facade dominates the cityscape, flanked by other towering structures and cranes suggesting ongoing development.",
      ),
      Pair(
        "Twilight Serenity at Kiyomizu-dera",
        "A magnificent pagoda, likely part of Kiyomizu-dera Temple, stands tall with its distinctive red and black tiers against a fading sky.\n" +
          "Traditional Japanese temple buildings with intricate architecture and dark tiled roofs extend alongside a paved pathway.",
      ),
      Pair(
        "New York City's Enduring Skyline",
        "The iconic Empire State Building commands the center of a sprawling urban panorama, reaching towards a clear sky.\n" +
          "A dense forest of skyscrapers stretches into the distance, showcasing the architectural might of Manhattan.",
      ),
      Pair(
        "Misty Towers of the City",
        "Tall, modern skyscrapers pierce through a soft, enveloping mist or fog, their upper reaches disappearing into the hazy sky.\n" +
          "The lower floors of the buildings glow with warm, inviting lights, creating a striking contrast against the muted tones of the day.",
      ),
      Pair(
        "Niagara's Majestic Horseshoe Bend",
        "An awe-inspiring aerial view captures the immense power and beauty of Niagara Falls, with the Horseshoe Falls prominently featured.\n" +
          "The vibrant turquoise water cascades dramatically, creating a mist that reveals a faint rainbow arching over the falls.",
      ),
      Pair(
        "Misty Mountain Cascade",
        "A verdant mountain slope, heavily forested with lush greenery, disappears into a thick blanket of fog above.\n" +
          "A slender waterfall tumbles down the rocky terrain, its white cascade a prominent feature amidst the dark stone.",
      ),
      Pair(
        "Niagara Falls",
        "The powerful Horseshoe Falls of Niagara dramatically plunges into a misty abyss, showcasing its immense volume.\n" +
          "The vibrant turquoise-green water at the brink contrasts with the deep, turbulent blues of the river above.",
      ),
      Pair(
        "Canyon's Turquoise Embrace",
        "An aerial perspective reveals a stunning, winding river with vibrant turquoise waters cutting through a rugged canyon.\n" +
          "The arid, textured slopes of the canyon contrast sharply with patches of green vegetation within the bend of the river.",
      ),
      Pair(
        "Aquatic Art: The Boat's Trail",
        "An aerial view captures a boat carving a dramatic, arcing wake across deep, dark water.\n" +
          "The white foam traces a beautiful, almost calligraphic pattern, contrasting sharply with the mysterious depths below.",
      ),
    )

  val samplePicsData =
    ctx.assets.list("sample_pics")?.sorted()?.mapIndexed { index, fileName ->
      ImageData(
        title = imageDetailsList[index].first,
        desc = imageDetailsList[index].second,
        bitmap = BitmapFactory.decodeStream(ctx.assets.open("sample_pics/$fileName")),
      )
    } ?: listOf()
  val pageCount = samplePicsData.size
  val pagerState = rememberPagerState { pageCount }
  var currentPage by remember { mutableIntStateOf(0) }

  var offsetY by remember { mutableFloatStateOf(0f) }

  var swipeDirection by remember { mutableStateOf(SwipeDirection.NONE) }

  LaunchedEffect(Unit) {
    snapshotFlow {
        // Combine the current page and its offset for detailed scroll information
        pagerState.currentPage to pagerState.currentPageOffsetFraction
      }
      .collect { (_, offsetFraction) ->
        // Determine swipe direction based on changes
        if (offsetFraction > 0f) {
          // Swiping right (towards the next page) or settling on a page to the right
          // The offsetFraction will be positive when moving from left to right
          // If page is currentPage, and offsetFraction is increasing, it's a right swipe
          swipeDirection = SwipeDirection.RIGHT
        } else if (offsetFraction < 0f) {
          // Swiping left (towards the previous page) or settling on a page to the left
          // The offsetFraction will be negative when moving from right to left
          // If page is currentPage, and offsetFraction is decreasing, it's a left swipe
          swipeDirection = SwipeDirection.LEFT
        } else {
          swipeDirection = SwipeDirection.NONE
        }

        // You can also compare current `page` with a previously stored page index
        // to detect a full page change and its direction.
        // Example:
        // val previousPage = remember { mutableStateOf(pagerState.currentPage) }
        // if (page > previousPage.value) {
        //     // Swiped right to a new page
        // } else if (page < previousPage.value) {
        //     // Swiped left to a new page
        // }
        // previousPage.value = page
        Log.d("SwipeDirection", "$swipeDirection")
      }
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .collect {
        currentPage = it
        Log.d("PagerState", "${pagerState.endOffsetForPage(currentPage)}")
      }
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.targetPage }
      .collect {
        Log.d("PagerStateSettledPage", "${it}")
      }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    HorizontalPager(
      modifier =
        Modifier
          .weight(1f)
          .pointerInteropFilter {
            offsetY = it.y
            false
          }
          .clip(RoundedCornerShape(25.dp)),
      state = pagerState,
      pageContent = { page ->
        val circularRevealModifier =
          Modifier.graphicsLayer {
            // MAKE THE PAGE NOT MOVE
            // For Current page it'll be from 0 to screen width. For next page screen width to 0.
            val pageOffset = pagerState.offsetForPage(page)
            translationX = size.width * pageOffset

            // ADD THE CIRCULAR CLIPPING
            val endOffset = pagerState.endOffsetForPage(page)

            shadowElevation = 20f
            shape =
              CirclePath(progress = 1f - endOffset.absoluteValue, origin = Offset(size.width, offsetY))
            clip = true

            // PARALLAX SCALING
            val absoluteOffset = pagerState.offsetForPage(page).absoluteValue
            val scale = 1f + (absoluteOffset.absoluteValue * .4f)

            scaleX = scale
            scaleY = scale

            // FADE AWAY
            val startOffset = pagerState.startOffsetForPage(page)
//            alpha = (2f - startOffset) / 2f
          }

        Box(modifier = Modifier
          .fillMaxSize()
          .then(circularRevealModifier)) {
          AsyncImage(
            model = samplePicsData[page].bitmap,
            contentDescription = "",
            contentScale = ContentScale.Crop,
          )

          Column(
            modifier =
              Modifier
                .fillMaxWidth()
                .background(
                  brush =
                    Brush.verticalGradient(
                      colors = listOf(Color.Black, Color.Transparent),
                      startY = 475f,
                      endY = 0f,
                    )
                )
                .padding(16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            BasicText(
              text = samplePicsData[page].title,
              style =
                MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = Color.White,
                  shadow = Shadow(color = Color.Black, offset = Offset(2f, 4f), blurRadius = 8f),
                ),
            )

            Text(
              text = samplePicsData[page].desc,
              style = MaterialTheme.typography.bodyMedium,
              color = Color.White,
              fontWeight = FontWeight.Light,
            )
          }
        }
      },
    )
  }
}

class CirclePath(
  private val progress: Float, // From 0f to 1f
  private val origin: Offset = Offset(0f, 0f),
) : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density,
  ): Outline {
    val center =
      Offset(
        x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
        y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
      )
    Log.d("centerX", "${size.center.x}") // 540f
    Log.d("centerY", "${size.center.y}") // 1170f
    Log.d("progress", "$progress") // 0f to 1f. 0.500
    Log.d("originX", "${origin.x}") // 1080f
    Log.d("originY", "${origin.y}") // 1842f

    val radius = (sqrt(size.height * size.height + size.width * size.width) * .5f) * progress

    return Outline.Generic(Path().apply { addOval(Rect(center = center, radius = radius)) })
  }
}

// ACTUAL OFFSET
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
fun PagerState.startOffsetForPage(page: Int): Float {
  return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
fun PagerState.endOffsetForPage(page: Int): Float {
  return offsetForPage(page).coerceAtMost(0f)
}

enum class SwipeDirection {
  LEFT,
  RIGHT,
  NONE,
}
