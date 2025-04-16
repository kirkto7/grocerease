package edu.msoe.grocerease


data class Recipe(
    val title: String,
    val imageResId: Int,
    val ingredients: List<String>,
    val steps: List<String>
)