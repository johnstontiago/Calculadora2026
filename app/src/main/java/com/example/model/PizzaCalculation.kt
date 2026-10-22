package com.example.model

data class IngredientItem(
  val name: String,
  val amountGrams: Double,
  val percentage: Double? = null,
  val unit: String = "g"
) {
  val formattedAmount: String
    get() = if (amountGrams % 1.0 == 0.0) {
      amountGrams.toInt().toString()
    } else {
      String.format(java.util.Locale.US, "%.1f", amountGrams)
    }

  val formattedPercentage: String?
    get() = percentage?.let {
      if (it % 1.0 == 0.0) {
        "${it.toInt()}%"
      } else {
        String.format(java.util.Locale.US, "%.1f%%", it)
      }
    }
}

data class RecipePhase(
  val stepNumber: Int,
  val title: String,
  val timeDuration: String? = null,
  val ingredients: List<IngredientItem> = emptyList(),
  val instruction: String? = null
)

data class CalculationResult(
  val pizzas: Int,
  val weightPerBall: Int,
  val hydration: Int,
  val technique: FermentationTechnique,
  val yeastType: YeastType,
  val totalFlour: Int,
  val totalWater: Int,
  val totalSalt: Int,
  val totalYeast: Double,
  val totalDoughWeight: Int,
  val calculatedWaterTemp: Int,
  val ambientTemp: Int,
  val flourTemp: Int,
  val phases: List<RecipePhase>,
  val avpnNote: String,
  val calculatedAtMillis: Long = System.currentTimeMillis()
)

data class SavedRecipe(
  val id: String = java.util.UUID.randomUUID().toString(),
  val title: String,
  val pizzas: Int,
  val weightPerBall: Int,
  val hydration: Int,
  val technique: FermentationTechnique,
  val yeastType: YeastType,
  val ambientTemp: Int = 22,
  val flourTemp: Int = 22,
  val savedAtMillis: Long = System.currentTimeMillis()
)
