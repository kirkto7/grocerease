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
import edu.msoe.grocerease.entities.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.coroutineContext

class MainViewModel (
    application: Application
) : AndroidViewModel(application) {

    private val apiKey = "591906a4e6cf4ef88fa71d0210c735b6"
    private val repo: RecipeRepo = RecipeRepo.get()


    fun mapToRoomEntities(spoonRecipe: SpoonacularRecipe): Triple<Recipe, List<Ingredient>, List<RecipeIngredientCrossRef>> {
        val recipeId = UUID.randomUUID()
        val ingredients = spoonRecipe.extendedIngredients.map {
            Ingredient(UUID.randomUUID(), it.name, it.amount, it.unit, false)
        }
        val crossRefs = ingredients.map {
            RecipeIngredientCrossRef(recipeId = recipeId, ingredientId = it.id)
        }
        val recipe = Recipe(
            id = recipeId,
            title = spoonRecipe.title,
            imageResURL = spoonRecipe.image ?: "",
            steps = spoonRecipe.instructions ?: ""
        )
        return Triple(recipe, ingredients, crossRefs)
    }

    suspend fun getRecipeWithIngredients(recipeId: UUID): RecipeWithIngredients? {
        var result: RecipeWithIngredients? = null
        val job = CoroutineScope(coroutineContext).launch {
            result = repo.getRecipeWithIngredients(recipeId)
        }
        job.join() // Wait for the coroutine to finish
        return result
    }

    suspend fun markIngredientsAsDisplayed(ingredientIds: List<UUID>) {
        val job = CoroutineScope(coroutineContext).launch {
            repo.markIngredientsAsDisplayed(ingredientIds)
        }
        job.join()
    }

    suspend fun insertIngredients(ingredients: List<Ingredient>) {
        val job = CoroutineScope(coroutineContext).launch {
            repo.insertIngredients(ingredients)
        }
        job.join() // Wait for the coroutine to finish
    }

    suspend fun getAllIngredients(): List<Ingredient>{
        var result: List<Ingredient> = emptyList()
        val job = CoroutineScope(coroutineContext).launch {
            result = repo.getAllIngredients()
        }
        job.join() // Wait for the coroutine to finish
        return result
    }

    suspend fun resetAllIngredientsDisplayed() {
        val job = CoroutineScope(coroutineContext).launch {
            repo.resetAllIngredientDisplayFlags()
        }
        job.join()
    }

    suspend fun getDisplayedIngredients(): List<Ingredient>{
        var result: List<Ingredient> = emptyList()
        val job = CoroutineScope(coroutineContext).launch {
            result = repo.getDisplayedIngredients()
        }
        job.join() // Wait for the coroutine to finish
        return result
    }

    suspend fun getAllRecipes(): List<Recipe> {
        var recipes: List<Recipe> = emptyList()
        var job = CoroutineScope(coroutineContext).launch {
            recipes = repo.getAllRecipes()
        }
        job.join()
        return recipes
    }

    suspend fun deleteAllIngredients() {
        var job = CoroutineScope(coroutineContext).launch {
            repo.deleteAllIngredients()
        }
        job.join()
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
                }
            } catch (e: Exception) {
                // Handle error (e.g. log, show message, etc.)
                Log.e("RecipeDebug", "Failed to fetch any recipes!")
            }
        }
    }
}