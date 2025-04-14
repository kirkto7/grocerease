package edu.msoe.grocerease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import edu.msoe.grocerease.R
import java.io.File

class SavedRecipesFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: RecipeImageAdapter
    private lateinit var imageFiles: List<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_recipes, container, false)

        viewPager = view.findViewById(R.id.recipeViewPager)

        // Load saved image files from internal storage
        val imagesDir = File(requireContext().filesDir, "recipes")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        imageFiles = imagesDir.listFiles()?.toList() ?: emptyList()

        adapter = RecipeImageAdapter(imageFiles)
        viewPager.adapter = adapter

        return view
    }
}