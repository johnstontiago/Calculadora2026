package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImpastoBlueLight
import com.example.ui.theme.ImpastoNavy
import kotlin.math.roundToInt

@Composable
fun WaterTempDialog(
  currentAmbient: Int,
  currentFlour: Int,
  isSpanish: Boolean,
  onDismiss: () -> Unit,
  onConfirm: (ambient: Int, flour: Int) -> Unit
) {
  var ambient by remember { mutableStateOf(currentAmbient.toFloat()) }
  var flour by remember { mutableStateOf(currentFlour.toFloat()) }

  val computedWater = (60 - flour.roundToInt() - ambient.roundToInt()).coerceIn(2, 18)

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isSpanish) "Ajustar Temperaturas" else "Ajustar Temperaturas",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = if (isSpanish) {
            "La fórmula de la AVPN (Regla del 60) calcula la temperatura del agua necesaria para que la masa final no sobrepase los 23°C:"
          } else {
            "A fórmula da AVPN (Regra dos 60) calcula a temperatura da água para que a massa final não passe de 23°C:"
          },
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ambient temperature slider
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isSpanish) "T. Ambiente:" else "T. Ambiente:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
          )
          Text(
            text = "${ambient.roundToInt()}°C",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ImpastoNavy
          )
        }
        Slider(
          value = ambient,
          onValueChange = { ambient = it },
          valueRange = 10f..38f,
          steps = 27,
          colors = SliderDefaults.colors(
            thumbColor = ImpastoNavy,
            activeTrackColor = ImpastoNavy
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Flour temperature slider
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isSpanish) "T. Harina:" else "T. Farinha:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
          )
          Text(
            text = "${flour.roundToInt()}°C",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ImpastoNavy
          )
        }
        Slider(
          value = flour,
          onValueChange = { flour = it },
          valueRange = 10f..38f,
          steps = 27,
          colors = SliderDefaults.colors(
            thumbColor = ImpastoNavy,
            activeTrackColor = ImpastoNavy
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "➡️ ${if (isSpanish) "Temperatura resultante del agua" else "Temperatura da água"}: $computedWater°C (máx. 18°C)",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = ImpastoBlueLight
        )
      }
    },
    confirmButton = {
      TextButton(onClick = { onConfirm(ambient.roundToInt(), flour.roundToInt()) }) {
        Text("Guardar", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar")
      }
    }
  )
}
