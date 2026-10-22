package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationResult
import com.example.ui.components.AvpnNoteCard
import com.example.ui.components.HeaderSection
import com.example.ui.components.HydrationSlider
import com.example.ui.components.NumberStepper
import com.example.ui.components.PhaseCard
import com.example.ui.components.TechniqueSelector
import com.example.ui.components.TotalsCard
import com.example.ui.components.WaterTempCard
import com.example.ui.components.YeastToggle
import com.example.ui.dialogs.AvpnGuideDialog
import com.example.ui.dialogs.SaveRecipeDialog
import com.example.ui.dialogs.SavedRecipesSheet
import com.example.ui.dialogs.WaterTempDialog
import com.example.ui.theme.ImpastoGold
import com.example.ui.theme.ImpastoNavy
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.WarnBgColor
import com.example.ui.theme.WarnBorderColor
import com.example.ui.theme.WarnColor
import com.example.viewmodel.PizzaCalculatorViewModel
import kotlinx.coroutines.launch

@Composable
fun PizzaCalculatorScreen(
  viewModel: PizzaCalculatorViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val savedRecipes by viewModel.savedRecipes.collectAsState()
  val scrollState = rememberScrollState()
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  LaunchedEffect(uiState.copiedSnackbarMessage) {
    uiState.copiedSnackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Mediterranean Header
      HeaderSection(
        isSpanish = uiState.isSpanish,
        onToggleLanguage = { viewModel.toggleLanguage() },
        onOpenSavedRecipes = { viewModel.setSavedRecipesOpen(true) },
        onOpenGuide = { viewModel.setGuideOpen(true) }
      )

      // Main Form Content (constrained to max width 560dp for clean tablet layout)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 560.dp)
          .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Section 1: Technique
        TechniqueSelector(
          selectedTechnique = uiState.technique,
          onSelectTechnique = { viewModel.setTechnique(it) },
          isSpanish = uiState.isSpanish
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: Quantities
        Text(
          text = if (uiState.isSpanish) "CANTIDAD Y GRAMAJE" else "QUANTIDADE E GRAMAGEM",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.8.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 4.dp)
        )

        NumberStepper(
          label = if (uiState.isSpanish) "Número de pizzas" else "Número de pizzas",
          sublabel = null,
          value = uiState.pizzas,
          unit = null,
          stepDelta = 1,
          minValue = 0,
          maxValue = 200,
          onValueChange = { viewModel.setPizzas(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        NumberStepper(
          label = if (uiState.isSpanish) "Peso por bola" else "Peso por bola",
          sublabel = if (uiState.isSpanish) "AVPN: 200–280 g recomendado" else "AVPN: 200–280 g recomendado",
          value = uiState.weightPerBall,
          unit = "g",
          stepDelta = 10,
          minValue = 0,
          maxValue = 400,
          onValueChange = { viewModel.setWeightPerBall(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section 3: Hydration
        HydrationSlider(
          hydration = uiState.hydration,
          onHydrationChange = { viewModel.setHydration(it) },
          isSpanish = uiState.isSpanish
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section 4: Yeast
        YeastToggle(
          selectedType = uiState.yeastType,
          onSelectType = { viewModel.setYeastType(it) },
          isSpanish = uiState.isSpanish
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Action Button
        Button(
          onClick = {
            viewModel.calculateDough()
            scope.launch {
              // Smooth scroll towards the result
              scrollState.animateScrollTo(scrollState.maxValue)
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ImpastoNavy),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("calculate_dough_button")
        ) {
          Text(
            text = if (uiState.isSpanish) "Calcular masa →" else "Calcular massa →",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }

        // Validation Error Box (if any)
        if (uiState.validationError != null) {
          Spacer(modifier = Modifier.height(14.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = WarnBgColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, WarnBorderColor),
            modifier = Modifier.fillMaxWidth().testTag("validation_alert")
          ) {
            Text(
              text = uiState.validationError!!,
              color = WarnColor,
              fontSize = 13.sp,
              lineHeight = 18.sp,
              modifier = Modifier.padding(12.dp)
            )
          }
        }

        // Results Section
        AnimatedVisibility(
          visible = uiState.calculationResult != null,
          enter = fadeIn() + expandVertically()
        ) {
          uiState.calculationResult?.let { result ->
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // Result Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = if (uiState.isSpanish) "Receta" else "Receita",
                    fontFamily = PlayfairDisplayFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImpastoNavy
                  )
                  val techName = if (uiState.isSpanish) result.technique.displayNameEs else result.technique.displayNamePt
                  Text(
                    text = "${result.pizzas} ${if (uiState.isSpanish) "pizzas" else "pizzas"} · ${result.weightPerBall}g · ${result.hydration}% ${if (uiState.isSpanish) "hidr." else "hidr."} · $techName",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Row {
                  // Share Button
                  IconButton(
                    onClick = {
                      val text = buildRecipeShareText(result, uiState.isSpanish)
                      shareRecipe(context, text)
                    },
                    modifier = Modifier.testTag("share_recipe_button")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Share,
                      contentDescription = "Compartir receta",
                      tint = ImpastoNavy
                    )
                  }

                  // Save Preset Button
                  IconButton(
                    onClick = { viewModel.setSaveDialogOpen(true) },
                    modifier = Modifier.testTag("save_recipe_button")
                  ) {
                    Icon(
                      imageVector = Icons.Default.BookmarkAdd,
                      contentDescription = "Guardar preajuste",
                      tint = ImpastoNavy
                    )
                  }
                }
              }

              HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.5.dp,
                modifier = Modifier.padding(vertical = 14.dp)
              )

              // Phases List
              result.phases.forEach { phase ->
                PhaseCard(phase = phase)
                Spacer(modifier = Modifier.height(10.dp))
              }

              // Totals Card
              TotalsCard(
                result = result,
                isSpanish = uiState.isSpanish
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Water Temperature Card
              WaterTempCard(
                result = result,
                isSpanish = uiState.isSpanish,
                onOpenTempTuner = { viewModel.setWaterTempDialogOpen(true) }
              )

              Spacer(modifier = Modifier.height(16.dp))

              // AVPN Specifications Note
              AvpnNoteCard(note = result.avpnNote)
            }
          }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Footer
        Text(
          text = if (uiState.isSpanish) {
            "Basado en el Disciplinare AVPN 2024 · Impasto v1.0"
          } else {
            "Baseado no Disciplinare AVPN 2024 · Impasto v1.0"
          },
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          letterSpacing = 0.5.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
        )
      }
    }
  }

  // Dialogs
  if (uiState.isWaterTempDialogOpen && uiState.calculationResult != null) {
    WaterTempDialog(
      currentAmbient = uiState.ambientTemp,
      currentFlour = uiState.flourTemp,
      isSpanish = uiState.isSpanish,
      onDismiss = { viewModel.setWaterTempDialogOpen(false) },
      onConfirm = { amb, fl ->
        viewModel.setTemperatures(amb, fl)
        viewModel.setWaterTempDialogOpen(false)
      }
    )
  }

  if (uiState.isSaveDialogOpen) {
    SaveRecipeDialog(
      defaultTitle = "${uiState.pizzas} pizzas ${uiState.weightPerBall}g - ${if (uiState.isSpanish) uiState.technique.displayNameEs else uiState.technique.displayNamePt}",
      isSpanish = uiState.isSpanish,
      onDismiss = { viewModel.setSaveDialogOpen(false) },
      onSave = { title -> viewModel.saveCurrentRecipe(title) }
    )
  }

  if (uiState.isSavedRecipesOpen) {
    SavedRecipesSheet(
      recipes = savedRecipes,
      isSpanish = uiState.isSpanish,
      onDismiss = { viewModel.setSavedRecipesOpen(false) },
      onSelectRecipe = { viewModel.loadSavedRecipe(it) },
      onDeleteRecipe = { viewModel.deleteSavedRecipe(it) }
    )
  }

  if (uiState.isGuideOpen) {
    AvpnGuideDialog(
      isSpanish = uiState.isSpanish,
      onDismiss = { viewModel.setGuideOpen(false) }
    )
  }
}

private fun buildRecipeShareText(result: CalculationResult, isSpanish: Boolean): String {
  val sb = StringBuilder()
  val tech = if (isSpanish) result.technique.displayNameEs else result.technique.displayNamePt
  val yeast = if (isSpanish) result.yeastType.displayNameEs else result.yeastType.displayNamePt

  sb.appendLine("🍕 Impasto · Receta Napolitana AVPN")
  sb.appendLine("${result.pizzas} pizzas de ${result.weightPerBall}g | Hidratación: ${result.hydration}%")
  sb.appendLine("Técnica: $tech | Levadura: $yeast (${result.yeastType.percentageText})")
  sb.appendLine()
  sb.appendLine("📋 INGREDIENTES TOTALES:")
  sb.appendLine("• Harina 00 (W 280–330): ${result.totalFlour} g (100%)")
  sb.appendLine("• Agua (${result.calculatedWaterTemp}°C): ${result.totalWater} g (${result.hydration}%)")
  sb.appendLine("• Sal fina: ${result.totalSalt} g (2.5%)")
  sb.appendLine("• Levadura: ${result.totalYeast} g")
  sb.appendLine("• Masa total: ${result.totalDoughWeight} g")
  sb.appendLine()
  sb.appendLine("PASOS DE PREPARACIÓN:")
  result.phases.forEach { phase ->
    val duration = phase.timeDuration?.let { " ($it)" } ?: ""
    sb.appendLine("Fase ${phase.stepNumber}: ${phase.title}$duration")
    phase.ingredients.forEach { ing ->
      val pct = ing.formattedPercentage?.let { " ($it)" } ?: ""
      sb.appendLine(" - ${ing.name}: ${ing.formattedAmount}${ing.unit}$pct")
    }
    if (phase.instruction != null) {
      sb.appendLine(" Instrucción: ${phase.instruction}")
    }
    sb.appendLine()
  }
  sb.appendLine("🌡️ Temperatura ideal del agua: ${result.calculatedWaterTemp}°C (Fórmula: 60 - T.harina - T.ambiente)")
  sb.appendLine(result.avpnNote)
  sb.appendLine("Generado con Impasto · Calculadora Napolitana")
  return sb.toString()
}

private fun shareRecipe(context: Context, text: String) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
  val clip = ClipData.newPlainText("Receta de Pizza", text)
  clipboard?.setPrimaryClip(clip)

  val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, text)
    type = "text/plain"
  }
  val shareIntent = Intent.createChooser(sendIntent, "Compartir receta")
  context.startActivity(shareIntent)
}
