package edu.msoe.grocerease.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.msoe.grocerease.R
import edu.msoe.grocerease.databinding.FragmentListBinding
import edu.msoe.grocerease.databinding.FragmentRecipeBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecipeFragment : Fragment() {

    private lateinit var addRecipeButton: Button
    private lateinit var savedRecipesButton: Button
    private var photoUri: Uri? = null

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                photoUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    saveImage(bitmap)
                    Toast.makeText(requireContext(), "Photo saved!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Photo capture cancelled.", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                saveImage(bitmap)
                Toast.makeText(requireContext(), "Image uploaded!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addRecipeButton = binding.addRecipeButton
        savedRecipesButton = binding.savedRecipesButton

        addRecipeButton.setOnClickListener {
            showImagePickerDialog()
        }

        savedRecipesButton.setOnClickListener {
            findNavController().navigate(R.id.savedRecipesFragment)
        }

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
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "RECIPE_$timeStamp.jpg"
        val storageDir = File(requireContext().filesDir, "recipes")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, imageFileName)
    }

    private fun saveImage(bitmap: Bitmap) {
        try {
            val file = createImageFile() ?: return
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        } catch (e: IOException) {
            Log.e("RecipeFragment", "Error saving image", e)
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}
