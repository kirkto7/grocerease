package edu.msoe.grocerease

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class RecipeImageAdapter(
    private val imageFiles: List<File>,
    private val titles: Map<String, String>,
    private val onImageClick: (File) -> Unit,
    private val onTitleClick: (File) -> Unit
) : RecyclerView.Adapter<RecipeImageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.recipeImage)
        val titleView: TextView = view.findViewById(R.id.recipeTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = imageFiles[position]
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        holder.imageView.setImageBitmap(bitmap)

        val title = titles[file.name] ?: "Sample Title"
        holder.titleView.text = title

        holder.imageView.setOnClickListener { onImageClick(file) }
        holder.titleView.setOnClickListener { onTitleClick(file) }
    }

    override fun getItemCount(): Int = imageFiles.size
}
