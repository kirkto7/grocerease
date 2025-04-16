package edu.msoe.grocerease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextItem: EditText
    private lateinit var addButton: Button
    private lateinit var adapter: GroceryAdapter
    private val groceryList = mutableListOf<GroceryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.findViewById(R.id.groceryRecyclerView)
        editTextItem = view.findViewById(R.id.editTextItem)
        addButton = view.findViewById(R.id.buttonAddItem)

        adapter = GroceryAdapter(groceryList) { index, isChecked ->
            groceryList[index].isChecked = isChecked
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addButton.setOnClickListener {
            val itemName = editTextItem.text.toString().trim()
            if (itemName.isNotEmpty()) {
                groceryList.add(GroceryItem(itemName))
                adapter.notifyItemInserted(groceryList.size - 1)
                editTextItem.text.clear()
            }
        }

        return view
    }
}
