package edu.msoe.grocerease.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.msoe.grocerease.R
import edu.msoe.grocerease.RecipeImageAdapter
import edu.msoe.grocerease.databinding.FragmentSavedRecipesBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedRecipesFragment : Fragment() {

    private var photoUri: Uri? = null
    private lateinit var imageFiles: MutableList<File>
    private lateinit var adapter: RecipeImageAdapter

    private var _binding: FragmentSavedRecipesBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val takePictureLauncher =

        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {

                refreshImages()
                Toast.makeText(requireContext(), "Photo saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Photo capture cancelled.", Toast.LENGTH_SHORT).show()
            }
        }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                saveImage(bitmap)
                refreshImages()
                Toast.makeText(requireContext(), "Image uploaded!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.savedRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        refreshImages()

        binding.addRecipeButton.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun saveTitleForImage(file: File, title: String) {
        val prefs = requireContext().getSharedPreferences("recipe_titles", 0)
        prefs.edit().putString(file.name, title).apply()
    }

    private fun loadTitles(): Map<String, String> {
        val prefs = requireContext().getSharedPreferences("recipe_titles", 0)
        return prefs.all.mapValues { it.value.toString() }
    }
    private fun showEditTitleDialog(file: File) {
        val editText = EditText(requireContext())
        editText.setText(loadTitles()[file.name] ?: "Sample Title")

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Recipe Title")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = editText.text.toString()
                saveTitleForImage(file, newTitle)
                refreshImages()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showImageDialog(file: File) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_full_image, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.fullImageView)

        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        imageView.setImageBitmap(bitmap)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .show()
    }


    private fun refreshImages() {
        val imagesDir = File(requireContext().filesDir, "recipes")
        if (!imagesDir.exists()) imagesDir.mkdirs()

        imageFiles = imagesDir.listFiles()?.toMutableList() ?: mutableListOf()

        val titles = loadTitles()

        adapter = RecipeImageAdapter(imageFiles, titles,
            onImageClick = { file -> showImageDialog(file) },
            onTitleClick = { file -> showEditTitleDialog(file) }
        )




        binding.savedRecipesRecyclerView.adapter = adapter
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Add Recipe Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> pickImageLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun launchCamera() {
        val imageFile = createImageFile() ?: return
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
        takePictureLauncher.launch(photoUri)
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "RECIPE_$timeStamp.jpg"
        val storageDir = File(requireContext().filesDir, "recipes")
        if (!storageDir.exists()) storageDir.mkdirs()
        return File(storageDir, fileName)
    }

    private fun saveImage(bitmap: Bitmap) {
        try {
            val file = createImageFile() ?: return
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        } catch (e: IOException) {
            Log.e("SavedRecipesFragment", "Error saving image", e)
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

