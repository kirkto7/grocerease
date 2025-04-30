package edu.msoe.grocerease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import edu.msoe.grocerease.R
import edu.msoe.grocerease.RecipeImageAdapter
import edu.msoe.grocerease.databinding.FragmentRecipeDetailBinding
import edu.msoe.grocerease.databinding.FragmentSavedRecipesBinding
import java.io.File

class SavedRecipesFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: RecipeImageAdapter
    private lateinit var imageFiles: List<File>


    private var _binding: FragmentSavedRecipesBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.recipeViewPager

        // Load saved image files from internal storage
        val imagesDir = File(requireContext().filesDir, "recipes")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        imageFiles = imagesDir.listFiles()?.toList() ?: emptyList()

        adapter = RecipeImageAdapter(imageFiles)
        viewPager.adapter = adapter

    }

}