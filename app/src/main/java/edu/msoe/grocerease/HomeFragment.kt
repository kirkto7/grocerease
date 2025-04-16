package edu.msoe.grocerease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageView>(R.id.imagePizza).setOnClickListener {
            navigateToRecipe(0)
        }
        view.findViewById<ImageView>(R.id.imageSalad).setOnClickListener {
            navigateToRecipe(1)
        }
        view.findViewById<ImageView>(R.id.imageBurger).setOnClickListener {
            navigateToRecipe(2)
        }
        view.findViewById<ImageView>(R.id.imagePasta).setOnClickListener {
            navigateToRecipe(3)
        }
    }

    private fun navigateToRecipe(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt("recipeId", recipeId)
        }
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }
}

