package edu.msoe.grocerease

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.msoe.grocerease.api.SpoonacularRecipe
import edu.msoe.grocerease.api.SpoonacularService
import edu.msoe.grocerease.database.RecipeRepo
import edu.msoe.grocerease.entities.Ingredient
import edu.msoe.grocerease.entities.Recipe
import edu.msoe.grocerease.entities.RecipeIngredientCrossRef
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel (
    application: Application
) : AndroidViewModel(application) {

    private val apiKey = "591906a4e6cf4ef88fa71d0210c735b6"
    private val repo: RecipeRepo = RecipeRepo.get()


    fun mapToRoomEntities(spoonRecipe: SpoonacularRecipe): Triple<Recipe, List<Ingredient>, List<RecipeIngredientCrossRef>> {
        val recipeId = UUID.randomUUID()
        val ingredients = spoonRecipe.extendedIngredients.map {
            Ingredient(UUID.randomUUID(), it.name, it.amount, it.unit)
        }
        val crossRefs = ingredients.map {
            RecipeIngredientCrossRef(recipeId = recipeId, ingredientId = it.id)
        }
        val recipe = Recipe(
            id = recipeId,
            title = spoonRecipe.title,
            imageResId = 0, // Change this if you add image caching
            steps = spoonRecipe.instructions ?: ""
        )
        return Triple(recipe, ingredients, crossRefs)
    }

    fun fetchAndStoreRecipes() {
        viewModelScope.launch {
            try {
                val response = SpoonacularService.api.getRandomRecipes(5, apiKey)
                response.recipes.forEach { spoonRecipe ->
                    val (recipe, ingredients, crossRefs) = mapToRoomEntities(spoonRecipe)
                    repo.insertRecipe(recipe)
                    repo.insertIngredients(ingredients)
                    repo.insertRecipeIngredientCrossRefs(crossRefs)

                    Log.d("RecipeDebug", "Recipe: ${recipe.title}\nSteps: ${recipe.steps}")
                    ingredients.forEach {
                        Log.d("RecipeDebug", "Ingredient: ${it.name} ${it.amount} ${it.unit}")
                    }
                }
            } catch (e: Exception) {
                // Handle error (e.g. log, show message, etc.)
            }
        }
    }
}