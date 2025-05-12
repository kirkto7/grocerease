package edu.msoe.grocerease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.msoe.grocerease.MainViewModel
import edu.msoe.grocerease.R
import edu.msoe.grocerease.RecipeAdapter
import edu.msoe.grocerease.databinding.FragmentHomeBinding
import edu.msoe.grocerease.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch
import java.util.UUID

class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recipeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.fetchAndStoreRecipes()
            val recipes = viewModel.getAllRecipes()

            val adapter = RecipeAdapter(recipes) { recipe ->
                navigateToRecipe(recipe.id)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun navigateToRecipe(recipeId: UUID) {
        val bundle = Bundle().apply {
            putString("recipeId", recipeId.toString()) // UUIDs passed as Strings
        }
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }
}
