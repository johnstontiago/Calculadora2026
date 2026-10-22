package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationResult
import com.example.model.IngredientItem
import com.example.model.YeastType
import com.example.ui.theme.ImpastoGoldDark
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.PlayfairDisplayFontFamily

@Composable
fun TotalsCard(
  result: CalculationResult,
  isSpanish: Boolean,
  modifier: Modifier = Modifier
) {
  val yeastLabel = if (result.yeastType == YeastType.FRESCO) {
    if (isSpanish) "Levadura fresca total" else "Fermento fresco"
  } else {
    if (isSpanish) "Levadura seca total" else "Fermento seco"
  }

  val totalIngredients = listOf(
    IngredientItem(if (isSpanish) "Harina 00 total" else "Farinha 00 total", result.totalFlour.toDouble()),
    IngredientItem(if (isSpanish) "Agua total" else "Água total", result.totalWater.toDouble()),
    IngredientItem(if (isSpanish) "Sal fino" else "Sal fino", result.totalSalt.toDouble()),
    IngredientItem(yeastLabel, result.totalYeast)
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
    border = BorderStroke(1.dp, ImpastoGoldDark.copy(alpha = 0.4f)),
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
        Text(
          text = if (isSpanish) "TOTAL DE INGREDIENTES" else "TOTAL DE INGREDIENTES",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.8.sp,
          color = ImpastoGoldDark
        )

        Text(
          text = "${result.totalDoughWeight}g ${if (isSpanish) "masa" else "massa"}",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = ImpastoGoldDark
        )
      }

      Column(modifier = Modifier.padding(top = 10.dp)) {
        totalIngredients.forEachIndexed { index, ingredient ->
          IngredientRow(ingredient = ingredient)
          if (index < totalIngredients.size - 1) {
            HorizontalDivider(
              color = ImpastoGoldDark.copy(alpha = 0.2f),
              thickness = 0.8.dp,
              modifier = Modifier.padding(vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}
