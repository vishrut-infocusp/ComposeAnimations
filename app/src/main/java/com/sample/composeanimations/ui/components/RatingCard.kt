package com.sample.composeanimations.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.composeanimations.models.RatingData

@Preview
@Composable
fun RatingCard(
  modifier: Modifier = Modifier,
  ratingData: RatingData =
    RatingData(
      userName = "FloydianDreamer",
      rating = "3.5",
      title = "An Unforgettable Night",
      description =
        "The Pink Floyd concert was an absolute spectacle! The light show, combined with their iconic music, created an atmosphere that I will never forget. The band's energy on stage was infectious.",
    ),
) {
  val shape = RoundedCornerShape(16.dp)
  val borderWidth = 1.dp
  val borderGradient = Brush.verticalGradient(colors = listOf(Color(0xFF716E70), Color(0xFF2C2A2C)))

  Column(
    modifier =
      modifier.then(
        Modifier.border(width = borderWidth, brush = borderGradient, shape = shape)
          .clip(shape)
          .background(color = Color(0xFF211F21))
          .fillMaxWidth()
          .padding(20.dp)
      ),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(20.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.border(width = borderWidth, brush = borderGradient, shape = CircleShape)
            .clip(CircleShape)
            .background(color = Color(0xFF393739))
            .size(42.dp)
      )

      Row {
        Text(
          text = ratingData.userName,
          style =
            MaterialTheme.typography.titleSmall.copy(
              color = Color(0xFF69676B),
              fontWeight = FontWeight.Bold,
            ),
        )

        Text(
          text = " · ${ratingData.rating}",
          style =
            MaterialTheme.typography.titleSmall.copy(
              color = Color(0xFF69676B),
              fontWeight = FontWeight.Bold,
            ),
        )
      }
    }

    Text(
      text = ratingData.title,
      style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFB9B3B7)),
    )

//    Text(
//      text = ratingData.description,
//      style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6C6A6D)),
//    )
  }
}
