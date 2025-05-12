package edu.msoe.grocerease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import coil.load
import edu.msoe.grocerease.MainViewModel
import edu.msoe.grocerease.R
import edu.msoe.grocerease.databinding.FragmentRecipeDetailBinding
import edu.msoe.grocerease.entities.Ingredient
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.msoe.grocerease.entities.RecipeWithIngredients

import kotlinx.coroutines.launch
import java.util.UUID

class RecipeDetailFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
//
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idString = arguments?.getString("recipeId")
        if (idString == null) {
            // Handle missing ID
            return
        }

        val recipeId = UUID.fromString(idString)
        var ingredients: List<String> // Store ingredients


        // Fetch recipe data
        lifecycleScope.launch {
            val recipeWithIngredients: RecipeWithIngredients? =
                viewModel.getRecipeWithIngredients(recipeId)

            recipeWithIngredients?.let { recipe ->
                // Populate UI
                binding.detailTitle.text = recipe.recipe.title
                binding.detailImage
                    .load(recipe.recipe.imageResURL)

                binding.detailIngredients.text =
                    recipe.ingredients.joinToString("\n") { "• ${it.name} (${it.amount} ${it.unit})" }

                binding.detailSteps.text =
                    recipe.recipe.steps.split("\n").mapIndexed { i, step -> "${i + 1}. $step" }
                        .joinToString("\n")



                val ingredientIds = recipeWithIngredients.ingredients.map { it.id }

                binding.addButton.setOnClickListener {
                    // Add ingredients to the grocery list
                    lifecycleScope.launch {
                        viewModel.markIngredientsAsDisplayed(ingredientIds)
                    }

                    val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                    bottomNav.selectedItemId = R.id.listFragment

                }
            }






        }


    }
}
