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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FermentationTechnique
import com.example.ui.theme.ImpastoBlueLight
import com.example.ui.theme.ImpastoNavy

@Composable
fun TechniqueSelector(
  selectedTechnique: FermentationTechnique,
  onSelectTechnique: (FermentationTechnique) -> Unit,
  isSpanish: Boolean,
  modifier: Modifier = Modifier
) {
  val techniques = FermentationTechnique.values()

  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = if (isSpanish) "TÉCNICA DE FERMENTACIÓN" else "TÉCNICA DE FERMENTAÇÃO",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.8.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
    )

    // 2x2 Grid using Rows
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      TechniqueCard(
        technique = techniques[0],
        isSelected = selectedTechnique == techniques[0],
        isSpanish = isSpanish,
        onClick = { onSelectTechnique(techniques[0]) },
        modifier = Modifier.weight(1f)
      )
      TechniqueCard(
        technique = techniques[1],
        isSelected = selectedTechnique == techniques[1],
        isSpanish = isSpanish,
        onClick = { onSelectTechnique(techniques[1]) },
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      TechniqueCard(
        technique = techniques[2],
        isSelected = selectedTechnique == techniques[2],
        isSpanish = isSpanish,
        onClick = { onSelectTechnique(techniques[2]) },
        modifier = Modifier.weight(1f)
      )
      TechniqueCard(
        technique = techniques[3],
        isSelected = selectedTechnique == techniques[3],
        isSpanish = isSpanish,
        onClick = { onSelectTechnique(techniques[3]) },
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
private fun TechniqueCard(
  technique: FermentationTechnique,
  isSelected: Boolean,
  isSpanish: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val borderColor = if (isSelected) ImpastoNavy else MaterialTheme.colorScheme.outline
  val borderWidth = if (isSelected) 2.dp else 1.dp
  val containerColor = if (isSelected) {
    ImpastoNavy.copy(alpha = 0.08f)
  } else {
    MaterialTheme.colorScheme.surface
  }

  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag("technique_${technique.id}"),
    shape = RoundedCornerShape(14.dp),
    color = containerColor,
    border = BorderStroke(borderWidth, borderColor),
    shadowElevation = if (isSelected) 2.dp else 1.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = technique.icon,
        fontSize = 26.sp,
        modifier = Modifier.padding(bottom = 6.dp)
      )
      Text(
        text = if (isSpanish) technique.displayNameEs else technique.displayNamePt,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) ImpastoNavy else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = if (isSpanish) technique.descEs else technique.descPt,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
    }
  }
}
