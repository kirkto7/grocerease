package edu.msoe.grocerease.database

import android.content.Context
import androidx.room.Room
import edu.msoe.grocerease.entities.Ingredient
import edu.msoe.grocerease.entities.Recipe
import edu.msoe.grocerease.entities.RecipeIngredientCrossRef
import edu.msoe.grocerease.entities.RecipeWithIngredients
import java.util.UUID

private const val DATABASE_NAME = "grocerease-database"

class RecipeRepo private constructor(context: Context) {

    private val database: TranscriptDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            TranscriptDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    // Insert a single recipe
    suspend fun insertRecipe(recipe: Recipe) {
        database.recipeDao().insertRecipe(recipe)
    }

    // Insert multiple recipes
    suspend fun insertRecipes(recipes: List<Recipe>) {
        database.recipeDao().insertRecipes(recipes)
    }

    // Insert a single ingredient
    suspend fun insertIngredient(ingredient: Ingredient) {
        database.ingredientDao().insertIngredient(ingredient)
    }

    // Insert multiple ingredients
    suspend fun insertIngredients(ingredients: List<Ingredient>) {
        database.ingredientDao().insertIngredients(ingredients)
    }

    // Insert multiple Recipe-Ingredient cross-references
    suspend fun insertRecipeIngredientCrossRefs(crossRefs: List<RecipeIngredientCrossRef>) {
        database.recipeIngredientDao().insertRecipeIngredientCrossRefs(crossRefs)
    }

    // Delete a specific Recipe-Ingredient cross-reference
    suspend fun deleteRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef) {
        database.recipeIngredientDao().deleteRecipeIngredientCrossRef(crossRef)
    }

    // Get a recipe along with its ingredients by recipe ID
    suspend fun getRecipeWithIngredients(recipeId: UUID): RecipeWithIngredients {
        return database.recipeDao().getRecipeWithIngredients(recipeId)
    }

    // Get ingredients by a list of UUIDs
    suspend fun getIngredients(ingredientIds: List<UUID>): List<Ingredient> {
        return database.ingredientDao().getIngredients(ingredientIds)
    }

    // Get all recipes from the database
    suspend fun getAllRecipes(): List<Recipe> {
        return database.recipeDao().getAllRecipes() // Assuming this function exists in your RecipeDao
    }

    companion object {
        private var INSTANCE: RecipeRepo? = null

        // Initialize the repository
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = RecipeRepo(context)
            }
        }

        // Get the instance of the repository
        fun get(): RecipeRepo {
            return INSTANCE ?: throw IllegalStateException("RecipeRepo must be initialized")
        }
    }
}
