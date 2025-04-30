package edu.msoe.grocerease

data class GroceryItem(
    val name: String,
    val amount: Double,
    val units: String,
    var isChecked: Boolean = false
)