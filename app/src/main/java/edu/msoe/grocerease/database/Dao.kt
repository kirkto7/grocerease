package edu.msoe.grocerease.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.msoe.grocerease.entities.Ingredient
import edu.msoe.grocerease.entities.Recipe
import edu.msoe.grocerease.entities.RecipeIngredientCrossRef
import edu.msoe.grocerease.entities.RecipeWithIngredients
import java.util.UUID

@Dao
interface RecipeDao {

    // Insert a recipe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<Recipe>

    // Insert a list of recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)



    // Get the Recipe with its associated Ingredients
    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: UUID): RecipeWithIngredients
}

@Dao
interface IngredientDao {

    // Insert an ingredient
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Query("UPDATE Ingredient SET displayed = 0")
    suspend fun resetAllDisplayedFlags()

    @Query("SELECT * FROM Ingredient")
    suspend fun getAllIngredients(): List<Ingredient>

    @Query("DELETE FROM ingredient")
    suspend fun deleteAllIngredients()

    @Query("SELECT * FROM Ingredient WHERE displayed = 1")
    suspend fun getDisplayedIngredients(): List<Ingredient>

    @Query("UPDATE Ingredient SET displayed = 1 WHERE id = :ingredientId")
    suspend fun markIngredientAsDisplayed(ingredientId: UUID)

    @Query("UPDATE Ingredient SET displayed = 1 WHERE id IN (:ingredientIds)")
    suspend fun markIngredientsAsDisplayed(ingredientIds: List<UUID>)


    // Insert a list of ingredientsA
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    // Get ingredients by their UUIDs
    @Query("SELECT * FROM Ingredient WHERE id IN (:ingredientIds)")
    suspend fun getIngredients(ingredientIds: List<UUID>): List<Ingredient>
}

@Dao
interface RecipeIngredientDao {

    // Insert the cross-reference table entries
    @Insert
    suspend fun insertRecipeIngredientCrossRefs(crossRefs: List<RecipeIngredientCrossRef>)

    // Delete a specific cross-reference entry
    @Delete
    suspend fun deleteRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef)
}
