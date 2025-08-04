package com.sample.composeanimations.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.sample.composeanimations.ui.theme.Indigo500
import com.sample.composeanimations.ui.theme.Purple400

@Preview(showBackground = true)
@Composable
fun PathSample() {
  val width = 200f
  val height = 200f

  Column(modifier = Modifier.fillMaxSize()) {
    Canvas(modifier = Modifier.weight(1f).fillMaxSize()){

      val initialX = (this.size.width - width) / 2
      val initialY = (this.size.height - height) / 2
      val widthToDraw = initialX + width
      val heightToDraw = initialY + height

      val path = Path()
      path.moveTo(initialX, initialY)
      path.lineTo(initialX, heightToDraw)
      path.lineTo(widthToDraw, heightToDraw)
      path.relativeLineTo(-((widthToDraw - initialX) / 2), -((heightToDraw - initialY) / 2))
      path.relativeLineTo((widthToDraw - initialX) / 2, -(heightToDraw - initialY) / 2)
      path.close()

      drawPath(
        path = path,
        brush = Brush.verticalGradient(colors = listOf(Purple400, Indigo500)),
        style =
          Stroke(
            width = 10f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(40f, 20f)),
          ),
      )
    }

    Canvas(modifier = Modifier.weight(1f).fillMaxSize()){

    }
  }
}
