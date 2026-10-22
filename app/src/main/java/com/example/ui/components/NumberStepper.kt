package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImpastoNavy

@Composable
fun NumberStepper(
  label: String,
  sublabel: String? = null,
  value: Int,
  unit: String? = null,
  stepDelta: Int = 1,
  minValue: Int = 1,
  maxValue: Int = 200,
  onValueChange: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var showDirectInputDialog by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = label,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (sublabel != null) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = sublabel,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Stepper control: [-] [value] [+]
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Minus button
          Box(
            modifier = Modifier
              .size(width = 40.dp, height = 42.dp)
              .clickable {
                val next = (value - stepDelta).coerceIn(minValue, maxValue)
                onValueChange(next)
              }
              .testTag("stepper_minus_${label.take(5)}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "−",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = ImpastoNavy
            )
          }

          // Value display (tappable to edit directly)
          Box(
            modifier = Modifier
              .size(width = 64.dp, height = 42.dp)
              .clickable { showDirectInputDialog = true }
              .background(MaterialTheme.colorScheme.surface)
              .testTag("stepper_value_${label.take(5)}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (value == 0) "0" else "$value${unit?.let { " $it" } ?: ""}",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = if (value == 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )
          }

          // Plus button
          Box(
            modifier = Modifier
              .size(width = 40.dp, height = 42.dp)
              .clickable {
                val next = if (value == 0) minValue else (value + stepDelta).coerceIn(minValue, maxValue)
                onValueChange(next)
              }
              .testTag("stepper_plus_${label.take(5)}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "+",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = ImpastoNavy
            )
          }
        }
      }
    }
  }

  if (showDirectInputDialog) {
    var textInput by remember { mutableStateOf(value.toString()) }
    AlertDialog(
      onDismissRequest = { showDirectInputDialog = false },
      title = { Text(text = label, fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(
            text = "Ingresa el valor ($minValue – $maxValue${unit?.let { " $it" } ?: ""}):",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it.filter { ch -> ch.isDigit() } },
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = {
                val n = textInput.toIntOrNull() ?: value
                onValueChange(n.coerceIn(minValue, maxValue))
                showDirectInputDialog = false
              }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("stepper_dialog_input")
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            val n = textInput.toIntOrNull() ?: value
            onValueChange(n.coerceIn(minValue, maxValue))
            showDirectInputDialog = false
          }
        ) {
          Text("Aceptar", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDirectInputDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}
