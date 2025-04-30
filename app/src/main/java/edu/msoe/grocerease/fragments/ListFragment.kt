package edu.msoe.grocerease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.grocerease.GroceryAdapter
import edu.msoe.grocerease.GroceryItem
import edu.msoe.grocerease.R
import edu.msoe.grocerease.databinding.FragmentListBinding
import edu.msoe.grocerease.databinding.FragmentRecipeDetailBinding

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextItem: EditText
    private lateinit var addButton: Button
    private lateinit var adapter: GroceryAdapter
    private val groceryList = mutableListOf<GroceryItem>()

    private val args: ListFragmentArgs by navArgs()

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
        addButton = binding.buttonAddItem

        adapter = GroceryAdapter(groceryList) { index, isChecked ->
            groceryList[index].isChecked = isChecked
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Rebuild grocery items from arguments if available
        if (args.ingredientNames.isNotEmpty()) {
            val names = args.ingredientNames
            val amounts = args.ingredientAmounts
            val units = args.ingredientUnits

            for (i in names.indices) {
                groceryList.add(
                    GroceryItem(
                        name = names[i],
                        amount = amounts[i].toDouble(),
                        units = units[i]
                    )
                )
            }
            adapter.notifyDataSetChanged()
        }

        addButton.setOnClickListener {
            val itemName = editTextItem.text.toString().trim()
            if (itemName.isNotEmpty()) {
                groceryList.add(GroceryItem(name = itemName, amount = 1.0, units = ""))
                adapter.notifyItemInserted(groceryList.size - 1)
                editTextItem.text.clear()
            }
        }
    }

        fun addItemsToGroceryList(ingredients: List<String>) {
        ingredients.forEach { ingredient ->
            // Here, you can parse ingredient text if needed, e.g., separate name, amount, etc.
            groceryList.add(GroceryItem(ingredient, amount = 0.0, units = "")) // Assuming simple ingredient name for now
        }
        adapter.notifyDataSetChanged() // Refresh RecyclerView
    }
}
