package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RecipeRepository
import com.example.model.CalculationResult
import com.example.model.FermentationTechnique
import com.example.model.PizzaCalculatorEngine
import com.example.model.SavedRecipe
import com.example.model.YeastType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorUiState(
  val pizzas: Int = 4,
  val weightPerBall: Int = 260,
  val hydration: Int = 62,
  val technique: FermentationTechnique = FermentationTechnique.DIRETA,
  val yeastType: YeastType = YeastType.FRESCO,
  val ambientTemp: Int = 22,
  val flourTemp: Int = 22,
  val isSpanish: Boolean = true,
  val validationError: String? = null,
  val calculationResult: CalculationResult? = null,
  val isSavedRecipesOpen: Boolean = false,
  val isGuideOpen: Boolean = false,
  val isSaveDialogOpen: Boolean = false,
  val isWaterTempDialogOpen: Boolean = false,
  val copiedSnackbarMessage: String? = null
)

class PizzaCalculatorViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = RecipeRepository(application.applicationContext)
  val savedRecipes: StateFlow<List<SavedRecipe>> = repository.savedRecipes

  private val _uiState = MutableStateFlow(CalculatorUiState())
  val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

  init {
    // Initial calculation with standard defaults (4 pizzas, 260g)
    calculateDough()
  }

  fun setPizzas(count: Int) {
    val clamped = count.coerceIn(0, 200)
    _uiState.update { it.copy(pizzas = clamped, validationError = null) }
    if (clamped > 0 && _uiState.value.weightPerBall > 0) {
      calculateDough()
    }
  }

  fun adjustPizzas(delta: Int) {
    val current = _uiState.value.pizzas
    val next = if (current == 0 && delta > 0) 1 else (current + delta).coerceIn(0, 200)
    setPizzas(next)
  }

  fun setWeightPerBall(weight: Int) {
    val clamped = weight.coerceIn(0, 400)
    _uiState.update { it.copy(weightPerBall = clamped, validationError = null) }
    if (_uiState.value.pizzas > 0 && clamped > 0) {
      calculateDough()
    }
  }

  fun adjustWeightPerBall(delta: Int) {
    val current = _uiState.value.weightPerBall
    val next = if (current == 0 && delta > 0) 200 else (current + delta).coerceIn(0, 400)
    setWeightPerBall(next)
  }

  fun setHydration(percentage: Int) {
    val clamped = percentage.coerceIn(50, 85)
    _uiState.update { it.copy(hydration = clamped) }
    if (_uiState.value.pizzas > 0 && _uiState.value.weightPerBall > 0) {
      calculateDough()
    }
  }

  fun setTechnique(technique: FermentationTechnique) {
    _uiState.update { it.copy(technique = technique) }
    if (_uiState.value.pizzas > 0 && _uiState.value.weightPerBall > 0) {
      calculateDough()
    }
  }

  fun setYeastType(yeastType: YeastType) {
    _uiState.update { it.copy(yeastType = yeastType) }
    if (_uiState.value.pizzas > 0 && _uiState.value.weightPerBall > 0) {
      calculateDough()
    }
  }

  fun setTemperatures(ambient: Int, flour: Int) {
    _uiState.update {
      it.copy(
        ambientTemp = ambient.coerceIn(10, 40),
        flourTemp = flour.coerceIn(10, 40)
      )
    }
    if (_uiState.value.pizzas > 0 && _uiState.value.weightPerBall > 0) {
      calculateDough()
    }
  }

  fun toggleLanguage() {
    _uiState.update { it.copy(isSpanish = !it.isSpanish) }
    if (_uiState.value.calculationResult != null) {
      calculateDough()
    }
  }

  fun calculateDough() {
    val s = _uiState.value
    if (s.pizzas <= 0 || s.weightPerBall <= 0) {
      val missing = mutableListOf<String>()
      if (s.pizzas <= 0) {
        missing.add(if (s.isSpanish) "la cantidad de pizzas" else "a quantidade de bolas")
      }
      if (s.weightPerBall <= 0) {
        missing.add(if (s.isSpanish) "el peso por bola" else "o peso por bola")
      }
      val msg = if (s.isSpanish) {
        "⚠️ Antes de calcular, ajusta " + missing.joinToString(" y ") + "."
      } else {
        "⚠️ Antes de calcular, ajuste " + missing.joinToString(" e ") + "."
      }
      _uiState.update { it.copy(validationError = msg, calculationResult = null) }
      return
    }

    val result = PizzaCalculatorEngine.calculate(
      pizzas = s.pizzas,
      weightPerBall = s.weightPerBall,
      hydration = s.hydration,
      technique = s.technique,
      yeastType = s.yeastType,
      ambientTemp = s.ambientTemp,
      flourTemp = s.flourTemp,
      isSpanish = s.isSpanish
    )
    _uiState.update { it.copy(validationError = null, calculationResult = result) }
  }

  fun loadSavedRecipe(recipe: SavedRecipe) {
    _uiState.update {
      it.copy(
        pizzas = recipe.pizzas,
        weightPerBall = recipe.weightPerBall,
        hydration = recipe.hydration,
        technique = recipe.technique,
        yeastType = recipe.yeastType,
        ambientTemp = recipe.ambientTemp,
        flourTemp = recipe.flourTemp,
        isSavedRecipesOpen = false,
        validationError = null
      )
    }
    calculateDough()
  }

  fun saveCurrentRecipe(title: String) {
    val s = _uiState.value
    val recipe = SavedRecipe(
      title = title.ifBlank { "Pizza ${s.pizzas}x${s.weightPerBall}g (${s.technique.name})" },
      pizzas = s.pizzas,
      weightPerBall = s.weightPerBall,
      hydration = s.hydration,
      technique = s.technique,
      yeastType = s.yeastType,
      ambientTemp = s.ambientTemp,
      flourTemp = s.flourTemp
    )
    viewModelScope.launch {
      repository.saveRecipe(recipe)
      _uiState.update {
        it.copy(
          isSaveDialogOpen = false,
          copiedSnackbarMessage = if (it.isSpanish) "¡Receta guardada!" else "Receita guardada!"
        )
      }
    }
  }

  fun deleteSavedRecipe(id: String) {
    viewModelScope.launch {
      repository.deleteRecipe(id)
    }
  }

  fun setSavedRecipesOpen(open: Boolean) {
    _uiState.update { it.copy(isSavedRecipesOpen = open) }
  }

  fun setGuideOpen(open: Boolean) {
    _uiState.update { it.copy(isGuideOpen = open) }
  }

  fun setSaveDialogOpen(open: Boolean) {
    _uiState.update { it.copy(isSaveDialogOpen = open) }
  }

  fun setWaterTempDialogOpen(open: Boolean) {
    _uiState.update { it.copy(isWaterTempDialogOpen = open) }
  }

  fun clearSnackbar() {
    _uiState.update { it.copy(copiedSnackbarMessage = null) }
  }

  fun setSnackbarMessage(message: String) {
    _uiState.update { it.copy(copiedSnackbarMessage = message) }
  }
}
