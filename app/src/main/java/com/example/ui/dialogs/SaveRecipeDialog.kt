package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveRecipeDialog(
  defaultTitle: String,
  isSpanish: Boolean,
  onDismiss: () -> Unit,
  onSave: (String) -> Unit
) {
  var title by remember { mutableStateOf(defaultTitle) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isSpanish) "Guardar receta" else "Guardar receita",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = if (isSpanish) "Nombre o descripción para tu preajuste:" else "Nome para o seu pré-ajuste:",
          fontSize = 13.sp,
          modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = { onSave(title) }),
          modifier = Modifier.fillMaxWidth().testTag("recipe_name_input")
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { onSave(title) },
        modifier = Modifier.testTag("recipe_save_confirm_btn")
      ) {
        Text(if (isSpanish) "Guardar" else "Guardar", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(if (isSpanish) "Cancelar" else "Cancelar")
      }
    }
  )
}
