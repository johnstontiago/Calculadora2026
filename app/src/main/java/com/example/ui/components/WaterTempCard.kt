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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationResult
import com.example.ui.theme.ImpastoBlueLight
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.PlayfairDisplayFontFamily

@Composable
fun WaterTempCard(
  result: CalculationResult,
  isSpanish: Boolean,
  onOpenTempTuner: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = ImpastoNavy.copy(alpha = 0.06f),
    border = BorderStroke(1.5.dp, ImpastoNavy.copy(alpha = 0.25f)),
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Text(
        text = "🌡️",
        fontSize = 28.sp,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "${result.calculatedWaterTemp}°C",
              fontFamily = PlayfairDisplayFontFamily,
              fontSize = 28.sp,
              fontWeight = FontWeight.Bold,
              color = ImpastoBlueLight,
              modifier = Modifier.testTag("water_temp_display")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isSpanish) "máx. 18°C" else "máx. 18°C",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 4.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, ImpastoBlueLight.copy(alpha = 0.5f)),
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable(onClick = onOpenTempTuner)
              .testTag("tune_temperatures_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Ajustar temperaturas",
                tint = ImpastoBlueLight,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isSpanish) "Ajustar" else "Ajustar",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ImpastoBlueLight
              )
            }
          }
        }

        Text(
          text = if (isSpanish) "Temperatura ideal del agua" else "Temperatura ideal da água",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = if (isSpanish) {
            "Fórmula: 60 − T.harina (${result.flourTemp}°C) − T.ambiente (${result.ambientTemp}°C) = ${result.calculatedWaterTemp}°C\n⚠️ El agua nunca debe superar 18°C · La masa no debe pasar de 23°C en el amasado."
          } else {
            "Fórmula: 60 − T.farinha (${result.flourTemp}°C) − T.ambiente (${result.ambientTemp}°C) = ${result.calculatedWaterTemp}°C\n⚠️ A água nunca deve ultrapassar 18°C · Massa não deve passar de 23°C durante o amassado."
          },
          fontSize = 11.sp,
          lineHeight = 16.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
