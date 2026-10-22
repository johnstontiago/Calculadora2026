package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IngredientItem
import com.example.model.RecipePhase
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.PlayfairDisplayFontFamily

@Composable
fun PhaseCard(
  phase: RecipePhase,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    shadowElevation = 1.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Phase Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(26.dp)
            .background(ImpastoNavy, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = phase.stepNumber.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = phase.title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )

        if (phase.timeDuration != null) {
          Text(
            text = phase.timeDuration,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Ingredients Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        phase.ingredients.forEachIndexed { index, ingredient ->
          IngredientRow(ingredient = ingredient)
          if (index < phase.ingredients.size - 1) {
            HorizontalDivider(
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
              thickness = 0.8.dp,
              modifier = Modifier.padding(vertical = 4.dp)
            )
          }
        }

        if (phase.instruction != null) {
          Box(
            modifier = Modifier
              .padding(top = 10.dp)
              .fillMaxWidth()
              .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
              )
              .padding(10.dp)
          ) {
            Text(
              text = phase.instruction,
              fontSize = 12.sp,
              lineHeight = 17.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun IngredientRow(
  ingredient: IngredientItem,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = ingredient.name,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      )
      if (ingredient.formattedPercentage != null) {
        Text(
          text = ingredient.formattedPercentage!!,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Row(verticalAlignment = Alignment.Bottom) {
      Text(
        text = ingredient.formattedAmount,
        fontFamily = PlayfairDisplayFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = ImpastoNavy
      )
      Text(
        text = ingredient.unit,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 2.dp, start = 3.dp)
      )
    }
  }
}
