package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImpastoGold
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.ImpastoNavyDark
import com.example.ui.theme.ImpastoNavyLight
import com.example.ui.theme.PlayfairDisplayFontFamily

@Composable
fun HeaderSection(
  isSpanish: Boolean,
  onToggleLanguage: () -> Unit,
  onOpenSavedRecipes: () -> Unit,
  onOpenGuide: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(ImpastoNavyDark, ImpastoNavy)
        )
      )
      .drawBehind {
        // Geometric Mediterranean tile pattern
        val step = 44.dp.toPx()
        val strokeColor = Color(0x14FFFFFF)
        val circleColor = Color(0x18FFFFFF)
        var x = 0f
        while (x <= size.width) {
          drawLine(
            color = strokeColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f
          )
          x += step
        }
        var y = 0f
        while (y <= size.height) {
          drawLine(
            color = strokeColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f
          )
          y += step
        }
        x = step / 2
        while (x <= size.width) {
          y = step / 2
          while (y <= size.height) {
            drawCircle(
              color = circleColor,
              radius = 8.dp.toPx(),
              center = Offset(x, y),
              style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
            )
            y += step
          }
          x += step
        }
      }
      .statusBarsPadding()
      .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top utility buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Language switcher chip
        Row(
          modifier = Modifier
            .background(Color(0x33FFFFFF), RoundedCornerShape(16.dp))
            .clickable(onClick = onToggleLanguage)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("lang_toggle_button"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = ImpastoGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isSpanish) "ES" else "PT",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onOpenSavedRecipes,
            modifier = Modifier.testTag("saved_recipes_button")
          ) {
            Icon(
              imageVector = Icons.Default.Bookmark,
              contentDescription = "Recetas guardadas",
              tint = ImpastoGold
            )
          }
          IconButton(
            onClick = onOpenGuide,
            modifier = Modifier.testTag("guide_button")
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Guía AVPN",
              tint = Color(0xCCFFFFFF)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Logo Icon + Wordmark
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .background(Color(0x33FFFFFF), CircleShape)
            .border(1.5.dp, Color(0x66FFFFFF), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "🍕", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Impasto",
          fontFamily = PlayfairDisplayFontFamily,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xEEFFFFFF),
          letterSpacing = 0.5.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Main Headline
      Text(
        text = if (isSpanish) "Calculadora" else "Calculadora",
        fontFamily = PlayfairDisplayFontFamily,
        fontSize = 34.sp,
        fontWeight = FontWeight.Black,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 36.sp
      )
      Text(
        text = "Napolitana",
        fontFamily = PlayfairDisplayFontFamily,
        fontSize = 34.sp,
        fontWeight = FontWeight.Black,
        color = ImpastoGold,
        textAlign = TextAlign.Center,
        lineHeight = 36.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // AVPN Badge
      Box(
        modifier = Modifier
          .border(1.dp, ImpastoGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
          .background(Color(0x1AC8A96E), RoundedCornerShape(20.dp))
          .padding(horizontal = 14.dp, vertical = 5.dp)
      ) {
        Text(
          text = "AVPN · DISCIPLINARE 2024",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.6.sp,
          color = ImpastoGold
        )
      }
    }

    // Gold decorative bottom accent bar
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .height(3.dp)
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(ImpastoGold, ImpastoNavyLight, ImpastoGold)
          )
        )
    )
  }
}
