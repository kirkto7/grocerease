package edu.msoe.grocerease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recipeId = arguments?.getInt("recipeId") ?: 0


        val recipe = when (recipeId) {
            0 -> RecipeData("Pizza", R.drawable.pizza,
                listOf("Dough", "Tomato sauce", "Cheese"),
                listOf("Roll out dough", "Add sauce", "Add cheese", "Bake at 425°F for 15 mins"))
            1 -> RecipeData("Salad", R.drawable.salad,
                listOf("Lettuce", "Tomatoes", "Cucumber", "Dressing"),
                listOf("Chop veggies", "Mix in bowl", "Add dressing"))
            2 -> RecipeData("Burger", R.drawable.burger,
                listOf("Buns", "Beef patty", "Lettuce", "Cheese"),
                listOf("Grill patty", "Assemble burger"))
            3 -> RecipeData("Pasta", R.drawable.pasta,
                listOf("Pasta", "Sauce", "Parmesan"),
                listOf("Boil pasta", "Heat sauce", "Combine and top with cheese"))
            else -> null
        }

        recipe?.let {
            view.findViewById<ImageView>(R.id.detailImage).setImageResource(it.imageResId)
            view.findViewById<TextView>(R.id.detailTitle).text = it.title
            view.findViewById<TextView>(R.id.detailIngredients).text = it.ingredients.joinToString("\n") { ing -> "• $ing" }
            view.findViewById<TextView>(R.id.detailSteps).text = it.steps.mapIndexed { i, step -> "${i + 1}. $step" }.joinToString("\n")
        }
    }

    data class RecipeData(
        val title: String,
        val imageResId: Int,
        val ingredients: List<String>,
        val steps: List<String>
    )
}
