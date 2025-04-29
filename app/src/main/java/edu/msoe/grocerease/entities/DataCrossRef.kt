package edu.msoe.grocerease.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import java.util.UUID

@Entity(primaryKeys = ["recipeId", "ingredientId"])
data class RecipeIngredientCrossRef(
    val recipeId: UUID,
    val ingredientId: UUID
)

@Entity
data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id", // from Recipe
        entityColumn = "id", // from Ingredient
        associateBy = Junction(
            value = RecipeIngredientCrossRef::class,
            parentColumn = "recipeId",      // FK in junction referencing Recipe.id
            entityColumn = "ingredientId"   // FK in junction referencing Ingredient.id
        )
    )
    val ingredients: List<Ingredient>
)