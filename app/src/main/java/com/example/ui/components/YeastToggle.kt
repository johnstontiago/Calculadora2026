package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.YeastType
import com.example.ui.theme.ImpastoNavy

@Composable
fun YeastToggle(
  selectedType: YeastType,
  onSelectType: (YeastType) -> Unit,
  isSpanish: Boolean,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = if (isSpanish) "TIPO DE LEVADURA" else "TIPO DE FERMENTO",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.8.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )

    Surface(
      shape = RoundedCornerShape(10.dp),
      color = MaterialTheme.colorScheme.surfaceVariant,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(modifier = Modifier.fillMaxWidth()) {
        YeastOption(
          type = YeastType.FRESCO,
          isSelected = selectedType == YeastType.FRESCO,
          isSpanish = isSpanish,
          onClick = { onSelectType(YeastType.FRESCO) },
          modifier = Modifier.weight(1f)
        )
        YeastOption(
          type = YeastType.SECO,
          isSelected = selectedType == YeastType.SECO,
          isSpanish = isSpanish,
          onClick = { onSelectType(YeastType.SECO) },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun YeastOption(
  type: YeastType,
  isSelected: Boolean,
  isSpanish: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val name = if (isSpanish) type.displayNameEs else type.displayNamePt
  val label = "${type.icon} $name (${type.percentageText})"

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .background(
        if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
      )
      .padding(vertical = 12.dp, horizontal = 8.dp)
      .testTag("yeast_${type.id}"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      fontSize = 13.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) ImpastoNavy else MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
