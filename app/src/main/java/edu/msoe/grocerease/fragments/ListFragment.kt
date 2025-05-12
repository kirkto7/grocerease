package edu.msoe.grocerease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.grocerease.GroceryAdapter
import edu.msoe.grocerease.GroceryItem
import edu.msoe.grocerease.MainViewModel
import edu.msoe.grocerease.R
import edu.msoe.grocerease.databinding.FragmentListBinding
import edu.msoe.grocerease.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextItem: EditText
    private lateinit var editTextItem2: EditText
    private lateinit var editTextItem3: EditText
    private lateinit var addButton: Button
    private lateinit var clearButton: Button

    private lateinit var adapter: GroceryAdapter
    private val groceryList = mutableListOf<GroceryItem>()
    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = binding.groceryRecyclerView
        editTextItem = binding.editTextItem
        editTextItem2 = binding.editTextItem2
        editTextItem3 = binding.editTextItem3
        addButton = binding.buttonAddItem
        clearButton = binding.clearButton

        adapter = GroceryAdapter(groceryList) { index, isChecked ->
            groceryList[index].isChecked = isChecked
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        displayIngredients(adapter)

        addButton.setOnClickListener {
            val itemName = editTextItem.text.toString().trim()
            val amount = editTextItem2.text.toString().trim()
            val units = editTextItem3.text.toString().trim()
            if (itemName.isNotEmpty()) {
                groceryList.add(GroceryItem(name = itemName, amount = amount.toDouble(), units = units))
                adapter.notifyItemInserted(groceryList.size - 1)
                editTextItem.text.clear()
                editTextItem2.text.clear()
                editTextItem3.text.clear()
            }
            adapter.notifyDataSetChanged()
        }

        clearButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.resetAllIngredientsDisplayed()
                groceryList.clear()
                displayIngredients(adapter)
            }
        }
    }

    fun displayIngredients(adapter: GroceryAdapter) {
        lifecycleScope.launch {
            val ingredients = viewModel.getDisplayedIngredients()
            for (i in ingredients) {
                groceryList.add(
                    GroceryItem(
                        name = i.name,
                        amount = i.amount,
                        units = i.unit
                    )
                )
            }
            adapter.notifyDataSetChanged()
        }
    }
}
