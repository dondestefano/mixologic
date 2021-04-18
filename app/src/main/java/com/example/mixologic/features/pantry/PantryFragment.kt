package com.example.mixologic.features.pantry

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Ingredient
import com.example.mixologic.features.create.IngredientAdapter
import com.example.mixologic.managers.LiquorManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PantryFragment : Fragment() {
    private val pantryViewModel: PantryViewModel by viewModels()

    private lateinit var pantryAdapter: IngredientAdapter
    private lateinit var addIngredientActionButton: FloatingActionButton

    private var pantryRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addIngredientActionButton = view.findViewById(R.id.addIngredientActionButton)
        addIngredientActionButton.setOnClickListener{
            showPantryPopup()
        }

        initRecyclerView()
        observeViewModel()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(pantryRecyclerView?.context)

        pantryRecyclerView = view?.findViewById(R.id.pantryRecyclerView)
        pantryRecyclerView?.layoutManager = layoutManager
        pantryAdapter = IngredientAdapter(true, editable = true)
        pantryRecyclerView?.adapter = pantryAdapter

        pantryAdapter.onDeleteListener = {ingredient ->
            if (ingredient != null) {
                onDelete(ingredient)
            }
        }
    }

    private fun observeViewModel() {
        pantryViewModel.calculateRemaining()
        pantryViewModel.pantryState.observe(viewLifecycleOwner, Observer {
            when (it) {
                FetchState.SUCCESS -> {
                    pantryAdapter.updateItemsToList(pantryViewModel.pantryList)
                }
            }
        })
    }

    private fun showPantryPopup() {
        val view = LayoutInflater.from(activity).inflate(R.layout.add_liquor_popup,null)

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        popupWindow.isFocusable = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }

        // Setup UI
        val unitSpinner = view.findViewById(R.id.unitSpinner) as Spinner
        val unitEditText = view.findViewById(R.id.unitEditText) as EditText
        val cancelButton = view.findViewById(R.id.cancelButton) as Button
        val addButton = view.findViewById(R.id.addButton) as Button


        unitSpinner.adapter = activity?.let {
                ArrayAdapter(it, android.R.layout.simple_list_item_1, LiquorManager.getUnits())
            }

        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

            val liquorSpinner = view.findViewById(R.id.liquorSpinner) as Spinner
            liquorSpinner.adapter =
                activity?.let {
                    ArrayAdapter(it, android.R.layout.simple_list_item_1, pantryViewModel.remainingList)
                }

            addButton.setOnClickListener{
                if (!unitEditText.text.isNullOrBlank()) {
                    val liquor = Ingredient(
                        liquorSpinner.selectedItem.toString(),
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                    )
                    pantryViewModel.savePantryItem(liquor)
                    popupWindow.dismiss()
                }
            }

        popupWindow.showAtLocation(
            pantryRecyclerView,
            Gravity.CENTER,
            0,
            0
        )
    }

    override fun onResume() {
        super.onResume()
        pantryViewModel.updatePantryList()
    }

    private fun onDelete(ingredient: Ingredient) {
        pantryViewModel.deletePantryItem(ingredient)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PantryFragment()
    }
}