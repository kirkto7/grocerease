package edu.msoe.grocerease.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.msoe.grocerease.entities.*

@Database(entities = [Recipe::class, Ingredient::class, RecipeIngredientCrossRef::class], version=3)
@TypeConverters(TypeConverter::class)
abstract class TranscriptDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao
}