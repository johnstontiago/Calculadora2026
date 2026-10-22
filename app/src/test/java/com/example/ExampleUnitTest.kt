package com.example

import com.example.model.FermentationTechnique
import com.example.model.PizzaCalculatorEngine
import com.example.model.YeastType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testDirectDoughCalculation() {
    val result = PizzaCalculatorEngine.calculate(
      pizzas = 4,
      weightPerBall = 260,
      hydration = 62,
      technique = FermentationTechnique.DIRETA,
      yeastType = YeastType.FRESCO,
      ambientTemp = 22,
      flourTemp = 22,
      isSpanish = true
    )

    assertEquals(1040, result.totalDoughWeight)
    // Baker's math: 1 + 0.62 + 0.025 + 0.002 = 1.647
    // Farinha = round(1040 / 1.647) = 631g
    assertEquals(631, result.totalFlour)
    assertEquals(391, result.totalWater) // 631 * 0.62 = 391.22 -> 391
    assertEquals(16, result.totalSalt)   // 631 * 0.025 = 15.775 -> 16
    assertEquals(1.3, result.totalYeast, 0.01) // 631 * 0.002 = 1.262 -> 1.3
    assertEquals(16, result.calculatedWaterTemp)
    assertEquals(3, result.phases.size)
  }

  @Test
  fun testBigaDoughCalculation() {
    val result = PizzaCalculatorEngine.calculate(
      pizzas = 6,
      weightPerBall = 270,
      hydration = 65,
      technique = FermentationTechnique.BIGA,
      yeastType = YeastType.FRESCO,
      ambientTemp = 24,
      flourTemp = 23,
      isSpanish = true
    )

    assertTrue(result.totalFlour > 0)
    assertTrue(result.totalWater > 0)
    // Water temperature: 60 - 23 - 24 = 13°C
    assertEquals(13, result.calculatedWaterTemp)
    assertEquals(3, result.phases.size)
    assertEquals("Biga (pre-fermento)", result.phases[0].title)
  }

  @Test
  fun testWaterTemperatureClamping() {
    // Very hot room: 35°C room, 30°C flour -> 60 - 30 - 35 = -5°C -> clamped to 2°C
    val hotResult = PizzaCalculatorEngine.calculate(
      pizzas = 2,
      weightPerBall = 250,
      hydration = 60,
      technique = FermentationTechnique.DIRETA,
      yeastType = YeastType.SECO,
      ambientTemp = 35,
      flourTemp = 30
    )
    assertEquals(2, hotResult.calculatedWaterTemp)

    // Very cold room: 10°C room, 10°C flour -> 60 - 10 - 10 = 40°C -> clamped to 18°C max
    val coldResult = PizzaCalculatorEngine.calculate(
      pizzas = 2,
      weightPerBall = 250,
      hydration = 60,
      technique = FermentationTechnique.DIRETA,
      yeastType = YeastType.SECO,
      ambientTemp = 10,
      flourTemp = 10
    )
    assertEquals(18, coldResult.calculatedWaterTemp)
  }
}
