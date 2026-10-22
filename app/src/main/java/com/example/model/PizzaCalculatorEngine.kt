package com.example.model

import kotlin.math.roundToInt

object PizzaCalculatorEngine {

  fun calculate(
    pizzas: Int,
    weightPerBall: Int,
    hydration: Int,
    technique: FermentationTechnique,
    yeastType: YeastType,
    ambientTemp: Int = 22,
    flourTemp: Int = 22,
    isSpanish: Boolean = true
  ): CalculationResult {
    val h = hydration / 100.0
    val massaTotal = pizzas * weightPerBall
    val salPct = 0.025 // 2.5%
    val levPct = if (yeastType == YeastType.FRESCO) 0.002 else 0.00067
    val fatorDiv = 1.0 + h + salPct + levPct

    val farinha = (massaTotal / fatorDiv).roundToInt()
    val agua = (farinha * h).roundToInt()
    val sal = (farinha * salPct).roundToInt()
    val lev = ((farinha * levPct * 10.0).roundToInt()) / 10.0

    // Water temperature rule of 60
    val rawTemp = 60 - flourTemp - ambientTemp
    val tAgua = rawTemp.coerceIn(2, 18)

    val avisoSeco = if (yeastType == YeastType.SECO) {
      if (isSpanish) {
        " 💧 Levadura seca: hidratar en un poco de agua a temperatura ambiente durante 5 min antes de usar."
      } else {
        " 💧 Fermento seco: hidrate em pouquíssima água em temperatura ambiente por 5 min antes de usar."
      }
    } else ""

    val yeastName = if (yeastType == YeastType.FRESCO) {
      if (isSpanish) "Levadura fresca" else "Fermento fresco"
    } else {
      if (isSpanish) "Levadura seca activa" else "Fermento seco"
    }

    val yeastPct = if (yeastType == YeastType.FRESCO) 0.2 else 0.07

    val phases = mutableListOf<RecipePhase>()
    val avpnNote: String

    when (technique) {
      FermentationTechnique.DIRETA -> {
        phases.add(
          RecipePhase(
            stepNumber = 1,
            title = if (isSpanish) "Mezcla directa" else "Mistura direta",
            timeDuration = null,
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Harina 00 (W 280–330)" else "Farinha 00 (W 280–330)",
                amountGrams = farinha.toDouble(),
                percentage = 100.0
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría" else "Água fria",
                amountGrams = agua.toDouble(),
                percentage = hydration.toDouble()
              ),
              IngredientItem(
                name = if (isSpanish) "Sal fina" else "Sal fino",
                amountGrams = sal.toDouble(),
                percentage = 2.5
              ),
              IngredientItem(
                name = yeastName,
                amountGrams = lev,
                percentage = yeastPct
              )
            ),
            instruction = if (isSpanish) {
              "Disuelve la levadura en un poco de agua fría.$avisoSeco Añade la harina gradualmente mientras amasas. A los 2/3 del amasado, incorpora la sal. Continúa amasando hasta que la masa quede lisa y elástica (~20 min en total). ⚠️ La temperatura de la masa no debe superar los 23°C."
            } else {
              "Dissolva o fermento em um pouco de água fria.$avisoSeco Adicione a farinha gradualmente enquanto amassa. Nos 2/3 do amassado, incorpore o sal. Continue amassando até a massa ficar lisa e elástica (~20 min total). ⚠️ Temperatura da massa não deve ultrapassar 23°C."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 2,
            title = if (isSpanish) "Fermentación en bloque" else "Fermentação em bloco",
            timeDuration = "60–100 min · 24–26°C",
            instruction = if (isSpanish) {
              "Unta ligeramente un bol con aceite de oliva. Transfiere la masa, cubre con film plástico y deja reposar a temperatura ambiente."
            } else {
              "Unte levemente uma tigela com azeite. Transfira a massa, cubra com plástico filme e deixe descansar em temperatura ambiente."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 3,
            title = if (isSpanish) "Boleado y maduración" else "Bolear e maturação",
            timeDuration = if (isSpanish) "6–8h (o 24h a ≤4°C)" else "6–8h (ou 24h ≤4°C)",
            instruction = if (isSpanish) {
              "Forma las $pizzas bolas de ${weightPerBall}g con tensión superficial. Cubre con film o caja de fermentación. Madurar en nevera si supera las 8h."
            } else {
              "Faça as $pizzas bolas de ${weightPerBall}g. Cubra com plástico. Maturar em geladeira se for mais de 8h."
            }
          )
        )
        avpnNote = if (isSpanish) {
          "📋 Disciplinare AVPN: harina W 280–330, fermentación total mínima 8h, grosor de borde 1–2cm, diámetro máx. 35cm."
        } else {
          "📋 Disciplinare AVPN: farinha W 280–330, fermentação total mínima 8h, espessura borda 1–2cm, diâmetro máx. 35cm."
        }
      }

      FermentationTechnique.BIGA -> {
        val bigaPct = 0.45
        val farinhaB = (farinha * bigaPct).roundToInt()
        val aguaB = (farinhaB * 0.44).roundToInt() // 44% hydration
        val levBiga = if (yeastType == YeastType.FRESCO) {
          ((farinhaB * 0.002 * 10.0).roundToInt()) / 10.0
        } else {
          ((farinhaB * 0.00067 * 10.0).roundToInt()) / 10.0
        }
        val farinhaR = farinha - farinhaB
        val aguaR = agua - aguaB

        val pctAguaResto = ((aguaR.toDouble() / farinha.toDouble()) * 1000.0).roundToInt() / 10.0

        phases.add(
          RecipePhase(
            stepNumber = 1,
            title = if (isSpanish) "Biga (pre-fermento)" else "Biga (pré-fermento)",
            timeDuration = "16–18h · 16–18°C",
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Harina 00 (para biga)" else "Farinha 00",
                amountGrams = farinhaB.toDouble(),
                percentage = 45.0
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría (44%)" else "Água fria",
                amountGrams = aguaB.toDouble(),
                percentage = 44.0
              ),
              IngredientItem(
                name = yeastName,
                amountGrams = levBiga,
                percentage = if (yeastType == YeastType.FRESCO) 1.0 else 0.3
              )
            ),
            instruction = if (isSpanish) {
              "Mezcla toscamente hasta que no quede harina seca (no desarrolles el gluten).$avisoSeco Cubre con plástico perforado o tela. Madurar 16–18h a 16–18°C."
            } else {
              "Misture grosseiramente (não desenvolva o glúten).$avisoSeco Cubra com plástico perfurado. Maturar 16–18h a 16–18°C."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 2,
            title = if (isSpanish) "Masa final" else "Massa final",
            timeDuration = null,
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Biga madurada (en trozos)" else "Biga maturada (quebrada em pedaços)",
                amountGrams = (farinhaB + aguaB).toDouble()
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría restante" else "Água fria",
                amountGrams = aguaR.toDouble(),
                percentage = pctAguaResto
              ),
              IngredientItem(
                name = if (isSpanish) "Harina 00 restante" else "Farinha 00 (restante)",
                amountGrams = farinhaR.toDouble(),
                percentage = 55.0
              ),
              IngredientItem(
                name = if (isSpanish) "Sal fina" else "Sal fino",
                amountGrams = sal.toDouble(),
                percentage = 2.5
              )
            ),
            instruction = if (isSpanish) {
              "Disuelve la biga en el agua fría, desmenuzándola bien con las manos. Incorpora la harina restante poco a poco mientras amasas. A los 2/3 del amasado, añade la sal. Amasa hasta obtener una masa lisa y homogénea (~15 min). ⚠️ La temperatura no debe superar los 23°C."
            } else {
              "Dissolva a biga na água fria, quebrando bem com as mãos até homogeneizar. Incorpore a farinha restante aos poucos enquanto amassa. Nos 2/3 do amassado, adicione o sal. Amasse até massa lisa (~15 min). ⚠️ Temperatura da massa não deve ultrapassar 23°C."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 3,
            title = if (isSpanish) "Boleado y maduración" else "Bolear e maturação",
            timeDuration = if (isSpanish) "60–100 min bloque + 4–5h bolas" else "60–100 min bloco + 4–5h bolas",
            instruction = if (isSpanish) {
              "Unta un bol con aceite, coloca la masa y cubre. Tras 60–100 min, forma las $pizzas bolas de ${weightPerBall}g. Cubre y deja reposar a temperatura ambiente."
            } else {
              "Unte uma tigela com azeite, coloque a massa e cubra. Após 60–100 min, forme $pizzas bolas de ${weightPerBall}g. Cubra e deixe em temperatura ambiente."
            }
          )
        )
        avpnNote = if (isSpanish) {
          "📋 La biga aporta mayor complejidad aromática y mejor estructura alveolar. La temperatura de maduración es crítica: por encima de 18°C acidifica en exceso."
        } else {
          "📋 A biga aporta maior complexidade aromática e melhor estrutura do miolo. A temperatura de maturação é crítica: acima de 18°C o pré-fermento acidifica demais."
        }
      }

      FermentationTechnique.POOLISH -> {
        val poolPct = 0.30
        val farinhaP = (farinha * poolPct).roundToInt()
        val aguaP = farinhaP // 100% hydration
        val levP = if (yeastType == YeastType.FRESCO) {
          ((farinhaP * 0.002 * 10.0).roundToInt()) / 10.0
        } else {
          ((farinhaP * 0.00067 * 10.0).roundToInt()) / 10.0
        }
        val farinhaR = farinha - farinhaP
        val aguaR = agua - aguaP
        val pctAguaResto = ((aguaR.toDouble() / farinha.toDouble()) * 1000.0).roundToInt() / 10.0

        phases.add(
          RecipePhase(
            stepNumber = 1,
            title = if (isSpanish) "Poolish (pre-fermento líquido)" else "Poolish (pré-fermento líquido)",
            timeDuration = "12–16h · 20°C",
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Harina 00 (para poolish)" else "Farinha 00",
                amountGrams = farinhaP.toDouble(),
                percentage = 30.0
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría (100%)" else "Água fria",
                amountGrams = aguaP.toDouble(),
                percentage = 100.0
              ),
              IngredientItem(
                name = yeastName,
                amountGrams = levP,
                percentage = if (yeastType == YeastType.FRESCO) 0.2 else 0.07
              )
            ),
            instruction = if (isSpanish) {
              "Mezcla bien hasta homogeneizar completamente.$avisoSeco Cubre y fermenta 12–16h a ~20°C. Estará listo cuando la superficie presente una red de burbujas y comience a hundirse ligeramente en el centro."
            } else {
              "Misture bem até homogeneizar.$avisoSeco Cubra e fermente 12–16h a 20°C. Pronto quando a superfície estiver reticular (cheia de bolhas) e começar a afundar no centro."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 2,
            title = if (isSpanish) "Masa final" else "Massa final",
            timeDuration = null,
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Poolish madurado" else "Poolish maturado",
                amountGrams = (farinhaP + aguaP).toDouble(),
                percentage = 30.0
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría (restante)" else "Água fria (restante)",
                amountGrams = aguaR.toDouble(),
                percentage = pctAguaResto
              ),
              IngredientItem(
                name = if (isSpanish) "Harina 00 (restante)" else "Farinha 00 (restante)",
                amountGrams = farinhaR.toDouble(),
                percentage = 70.0
              ),
              IngredientItem(
                name = if (isSpanish) "Sal fina" else "Sal fino",
                amountGrams = sal.toDouble(),
                percentage = 2.5
              )
            ),
            instruction = if (isSpanish) {
              "Disuelve el poolish en el agua restante. Incorpora la harina poco a poco mientras amasas. A los 2/3 del amasado, incorpora la sal. Amasa 12–15 min hasta conseguir masa lisa y elástica. ⚠️ La temperatura no debe superar los 23°C."
            } else {
              "Dissolva o poolish na água restante. Incorpore a farinha aos poucos enquanto amassa. Nos 2/3 do amassado, incorpore o sal. Amasse 12–15 min até massa lisa e elástica. ⚠️ Temperatura da massa não deve ultrapassar 23°C."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 3,
            title = if (isSpanish) "Boleado y maduración" else "Bolear e maturação",
            timeDuration = if (isSpanish) "60–100 min bloque + 4–5h bolas" else "60–100 min bloco + 4–5h bolas",
            instruction = if (isSpanish) {
              "Unta un bol con aceite, coloca la masa y cubre con plástico. Tras 60–100 min, forma las $pizzas bolas de ${weightPerBall}g. Cubre y reposa antes de estirar."
            } else {
              "Unte uma tigela com azeite, coloque a massa e cubra com plástico. Após 60–100 min, forme $pizzas bolas de ${weightPerBall}g. Cubra e descanse antes de abrir."
            }
          )
        )
        avpnNote = if (isSpanish) {
          "📋 El poolish produce masas con extensibilidad superior y notas de fermentación láctica muy agradables. Ideal para hidrataciones elevadas."
        } else {
          "📋 O poolish produz massa com extensibilidade superior e sabor ligeiramente ácido e láctico. Ideal para altas hidratações."
        }
      }

      FermentationTechnique.AUTOLISE -> {
        val aguaAuto = (agua * 0.80).roundToInt()
        val aguaResto = agua - aguaAuto
        val pctAguaAuto = ((aguaAuto.toDouble() / farinha.toDouble()) * 1000.0).roundToInt() / 10.0
        val pctAguaResto = ((aguaResto.toDouble() / farinha.toDouble()) * 1000.0).roundToInt() / 10.0

        phases.add(
          RecipePhase(
            stepNumber = 1,
            title = if (isSpanish) "Autólisis" else "Autólise",
            timeDuration = "30–60 min",
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Harina 00" else "Farinha 00",
                amountGrams = farinha.toDouble(),
                percentage = 100.0
              ),
              IngredientItem(
                name = if (isSpanish) "Agua fría (80%)" else "Água fria (80%)",
                amountGrams = aguaAuto.toDouble(),
                percentage = pctAguaAuto
              )
            ),
            instruction = if (isSpanish) {
              "Mezcla únicamente la harina con el 80% del agua fría (sin sal, sin levadura). La mezcla debe ser rústica. Cubre y reposa 30–60 min. El gluten se estructurará por hidratación natural."
            } else {
              "Misture apenas farinha e água (sem sal, sem fermento). A mistura deve ser grosseira. Cubra e repouse 30–60 min. O glúten se formará naturalmente."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 2,
            title = if (isSpanish) "Incorporación" else "Incorporação",
            timeDuration = null,
            ingredients = listOf(
              IngredientItem(
                name = if (isSpanish) "Agua fría (20% restante)" else "Água fria (restante 20%)",
                amountGrams = aguaResto.toDouble(),
                percentage = pctAguaResto
              ),
              IngredientItem(
                name = yeastName,
                amountGrams = lev,
                percentage = yeastPct
              ),
              IngredientItem(
                name = if (isSpanish) "Sal fina" else "Sal fino",
                amountGrams = sal.toDouble(),
                percentage = 2.5
              )
            ),
            instruction = if (isSpanish) {
              "Disuelve la levadura en el agua restante.$avisoSeco Añade a la masa autolizada. Amasa y a los 2/3 del proceso añade la sal. Continúa amasando hasta conseguir una masa elástica y sedosa (~10–12 min). ⚠️ Temperatura máxima de masa 23°C."
            } else {
              "Dissolva o fermento na água restante.$avisoSeco Adicione à massa autolisada. Amasse e, nos 2/3 do processo, incorpore o sal. Continue até massa lisa (~10–12 min). ⚠️ Temperatura da massa não deve ultrapassar 23°C."
            }
          )
        )
        phases.add(
          RecipePhase(
            stepNumber = 3,
            title = if (isSpanish) "Fermentación y bolas" else "Fermentação e bolas",
            timeDuration = if (isSpanish) "60–100 min bloque + 5–6h bolas" else "60–100 min bloco + 5–6h bolas",
            instruction = if (isSpanish) {
              "Unta un bol con aceite y transfiere la masa. Cubre y reposa 60–100 min. Bolear en $pizzas unidades de ${weightPerBall}g. Madurar tapado a 24°C."
            } else {
              "Unte uma tigela com azeite e transfira a massa. Cubra e descanse 60–100 min. Bolear em $pizzas unidades de ${weightPerBall}g. Maturar coberto a 24°C."
            }
          )
        )
        avpnNote = if (isSpanish) {
          "📋 La autólisis reduce el tiempo de amasado mecánico en ~30% y produce una masa de excelente extensibilidad, perfecta para estirar a mano sin que encoja."
        } else {
          "📋 A autólise reduz o tempo de amassamento em ~30% e produz massa com extensibilidade excepcional, ideal para abrir na mão sem retrair."
        }
      }
    }

    return CalculationResult(
      pizzas = pizzas,
      weightPerBall = weightPerBall,
      hydration = hydration,
      technique = technique,
      yeastType = yeastType,
      totalFlour = farinha,
      totalWater = agua,
      totalSalt = sal,
      totalYeast = lev,
      totalDoughWeight = massaTotal,
      calculatedWaterTemp = tAgua,
      ambientTemp = ambientTemp,
      flourTemp = flourTemp,
      phases = phases,
      avpnNote = avpnNote
    )
  }
}
