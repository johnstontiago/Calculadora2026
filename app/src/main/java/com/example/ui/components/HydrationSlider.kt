package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.PlayfairDisplayFontFamily
import kotlin.math.roundToInt

@Composable
fun HydrationSlider(
  hydration: Int,
  onHydrationChange: (Int) -> Unit,
  isSpanish: Boolean,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    shadowElevation = 1.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isSpanish) "Hidratación" else "Hidratação",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (isSpanish) "Porcentaje de agua / harina" else "Percentual de água / farinha",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "$hydration",
            fontFamily = PlayfairDisplayFontFamily,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ImpastoNavy,
            modifier = Modifier.testTag("hydration_value_display")
          )
          Text(
            text = "%",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 3.dp, start = 1.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Slider(
        value = hydration.toFloat(),
        onValueChange = { onHydrationChange(it.roundToInt()) },
        valueRange = 55f..75f,
        steps = 19, // step of 1%
        colors = SliderDefaults.colors(
          thumbColor = ImpastoNavy,
          activeTrackColor = ImpastoNavy,
          inactiveTrackColor = MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hydration_slider")
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Preset quick targets
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        PresetChip(
          text = if (isSpanish) "55% · Clásica" else "55% · Clássica",
          isSelected = hydration == 55,
          onClick = { onHydrationChange(55) }
        )
        PresetChip(
          text = "62% · AVPN base",
          isSelected = hydration == 62,
          onClick = { onHydrationChange(62) }
        )
        PresetChip(
          text = if (isSpanish) "70% · Alta" else "70% · Alta",
          isSelected = hydration == 70,
          onClick = { onHydrationChange(70) }
        )
      }
    }
  }
}

@Composable
private fun PresetChip(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Text(
    text = text,
    fontSize = 11.sp,
    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
    color = if (isSelected) ImpastoNavy else MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(vertical = 4.dp, horizontal = 2.dp)
  )
}
