package edu.msoe.grocerease

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.grocerease.R
import java.io.File

class RecipeImageAdapter(private val imageFiles: List<File>) :
    RecyclerView.Adapter<RecipeImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val file = imageFiles[position]
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        holder.recipeImageView.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int = imageFiles.size
}