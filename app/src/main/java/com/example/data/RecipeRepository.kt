package com.example.data

import android.content.Context
import com.example.model.FermentationTechnique
import com.example.model.SavedRecipe
import com.example.model.YeastType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class RecipeRepository(context: Context) {
  private val prefs = context.getSharedPreferences("impasto_recipes", Context.MODE_PRIVATE)
  private val _savedRecipes = MutableStateFlow<List<SavedRecipe>>(emptyList())
  val savedRecipes: StateFlow<List<SavedRecipe>> = _savedRecipes.asStateFlow()

  init {
    loadRecipes()
  }

  private fun loadRecipes() {
    val jsonString = prefs.getString("recipes_list", null)
    if (jsonString.isNullOrEmpty()) {
      // Provide default starter presets
      val defaultPresets = listOf(
        SavedRecipe(
          id = "preset_classic",
          title = "Clásica Napolitana (4 pizzas)",
          pizzas = 4,
          weightPerBall = 260,
          hydration = 62,
          technique = FermentationTechnique.DIRETA,
          yeastType = YeastType.FRESCO
        ),
        SavedRecipe(
          id = "preset_biga_party",
          title = "Biga de Fin de Semana (8 pizzas)",
          pizzas = 8,
          weightPerBall = 270,
          hydration = 65,
          technique = FermentationTechnique.BIGA,
          yeastType = YeastType.FRESCO
        ),
        SavedRecipe(
          id = "preset_poolish_crisp",
          title = "Poolish Alta Hidratación (6 pizzas)",
          pizzas = 6,
          weightPerBall = 260,
          hydration = 68,
          technique = FermentationTechnique.POOLISH,
          yeastType = YeastType.SECO
        )
      )
      _savedRecipes.value = defaultPresets
      saveToDisk(defaultPresets)
      return
    }

    try {
      val jsonArray = JSONArray(jsonString)
      val list = mutableListOf<SavedRecipe>()
      for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        list.add(
          SavedRecipe(
            id = obj.getString("id"),
            title = obj.getString("title"),
            pizzas = obj.getInt("pizzas"),
            weightPerBall = obj.getInt("weightPerBall"),
            hydration = obj.getInt("hydration"),
            technique = FermentationTechnique.valueOf(obj.getString("technique")),
            yeastType = YeastType.valueOf(obj.getString("yeastType")),
            ambientTemp = obj.optInt("ambientTemp", 22),
            flourTemp = obj.optInt("flourTemp", 22),
            savedAtMillis = obj.optLong("savedAtMillis", System.currentTimeMillis())
          )
        )
      }
      _savedRecipes.value = list
    } catch (e: Exception) {
      _savedRecipes.value = emptyList()
    }
  }

  fun saveRecipe(recipe: SavedRecipe) {
    val current = _savedRecipes.value.toMutableList()
    val index = current.indexOfFirst { it.id == recipe.id }
    if (index >= 0) {
      current[index] = recipe
    } else {
      current.add(0, recipe)
    }
    _savedRecipes.value = current
    saveToDisk(current)
  }

  fun deleteRecipe(id: String) {
    val current = _savedRecipes.value.filterNot { it.id == id }
    _savedRecipes.value = current
    saveToDisk(current)
  }

  private fun saveToDisk(recipes: List<SavedRecipe>) {
    val jsonArray = JSONArray()
    for (r in recipes) {
      val obj = JSONObject().apply {
        put("id", r.id)
        put("title", r.title)
        put("pizzas", r.pizzas)
        put("weightPerBall", r.weightPerBall)
        put("hydration", r.hydration)
        put("technique", r.technique.name)
        put("yeastType", r.yeastType.name)
        put("ambientTemp", r.ambientTemp)
        put("flourTemp", r.flourTemp)
        put("savedAtMillis", r.savedAtMillis)
      }
      jsonArray.put(obj)
    }
    prefs.edit().putString("recipes_list", jsonArray.toString()).apply()
  }
}
