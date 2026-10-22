package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SavedRecipe
import com.example.ui.theme.ImpastoNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRecipesSheet(
  recipes: List<SavedRecipe>,
  isSpanish: Boolean,
  onDismiss: () -> Unit,
  onSelectRecipe: (SavedRecipe) -> Unit,
  onDeleteRecipe: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
      Text(
        text = if (isSpanish) "Recetas y preajustes guardados" else "Receitas e pré-ajustes guardados",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = ImpastoNavy
      )

      Spacer(modifier = Modifier.height(14.dp))

      if (recipes.isEmpty()) {
        Text(
          text = if (isSpanish) "No tienes recetas guardadas aún." else "Não tem receitas guardadas ainda.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(vertical = 24.dp)
        )
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(recipes, key = { it.id }) { recipe ->
            SavedRecipeItem(
              recipe = recipe,
              isSpanish = isSpanish,
              onSelect = { onSelectRecipe(recipe) },
              onDelete = { onDeleteRecipe(recipe.id) }
            )
          }
          item {
            Spacer(modifier = Modifier.height(30.dp))
          }
        }
      }
    }
  }
}

@Composable
private fun SavedRecipeItem(
  recipe: SavedRecipe,
  isSpanish: Boolean,
  onSelect: () -> Unit,
  onDelete: () -> Unit
) {
  val techName = if (isSpanish) recipe.technique.displayNameEs else recipe.technique.displayNamePt

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onSelect)
      .testTag("saved_recipe_item_${recipe.id}"),
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = recipe.title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${recipe.pizzas} ${if (isSpanish) "pizzas" else "pizzas"} · ${recipe.weightPerBall}g · ${recipe.hydration}% · $techName (${recipe.yeastType.icon})",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
          onClick = onSelect,
          colors = ButtonDefaults.buttonColors(containerColor = ImpastoNavy),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.size(width = 80.dp, height = 36.dp)
        ) {
          Text(text = if (isSpanish) "Cargar" else "Carregar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier.padding(start = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
