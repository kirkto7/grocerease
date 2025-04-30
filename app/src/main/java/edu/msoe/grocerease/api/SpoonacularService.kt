package edu.msoe.grocerease.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpoonacularService {

    private const val BASE_URL = "https://api.spoonacular.com/"

    val api: SpoonacularApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularApi::class.java)
    }
}

interface SpoonacularApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String
    ): RecipeResponse
}

data class RecipeResponse(
    val recipes: List<SpoonacularRecipe>
)

data class SpoonacularRecipe(
    val id: Int,
    val title: String,
    val image: String?,
    val instructions: String?,
    val extendedIngredients: List<SpoonacularIngredient>
)

data class SpoonacularIngredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String
)
