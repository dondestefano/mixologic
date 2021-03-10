package com.example.mixologic.features.create

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Recipe

const val LIQUOR_KEY = "LIQUOR_KEY"
const val INGREDIENT_KEY = "INGREDIENT_KEY"

class CreateFragment : Fragment() {
    private val createViewModel: CreateViewModel by viewModels()

    private lateinit var liquorAdapter: CreateAdapter
    private lateinit var ingredientAdapter: CreateAdapter

    private lateinit var addLiquorButton: ImageView
    private lateinit var addIngredientButton: ImageView
    private lateinit var drinkNameEditText: EditText
    private lateinit var drinkInstructionsEditText: EditText
    private lateinit var submitButton: Button

    private var liquorRecyclerView: RecyclerView? = null
    private var ingredientRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addIngredientButton = view.findViewById(R.id.addIngredientButton)
        addLiquorButton = view.findViewById(R.id.addLiquorButton)
        drinkNameEditText = view.findViewById(R.id.drinkNameEditText)
        drinkInstructionsEditText = view.findViewById(R.id.instructionsEditText)
        submitButton = view.findViewById(R.id.submitButton)

        initButtons()
        initRecyclerViews()
    }

    private fun initButtons() {
        addLiquorButton.setOnClickListener{
            showPopUp(LIQUOR_KEY)
        }

        addIngredientButton.setOnClickListener{
            showPopUp(INGREDIENT_KEY)
        }

        submitButton.setOnClickListener() {
            createRecipe()
        }
    }

    private fun initRecyclerViews() {
        ingredientRecyclerView = view?.findViewById(R.id.ingredientsRecyclerView)
        ingredientRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        ingredientAdapter = CreateAdapter(false)
        ingredientRecyclerView?.adapter = ingredientAdapter

        liquorRecyclerView = view?.findViewById(R.id.liquorsRecyclerView)
        liquorRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        liquorAdapter = CreateAdapter(true)
        liquorRecyclerView?.adapter = liquorAdapter
    }

    private fun createRecipe() {
        val recipe = Recipe(
                drinkNameEditText.text.toString(),
                drinkInstructionsEditText.text.toString(),
                liquorAdapter.getIngredients(),
                ingredientAdapter.getIngredients()
        )

        Log.d("!!!", recipe.toString())
    }

    private fun showPopUp(type: String) {
        val view = if (type == LIQUOR_KEY) {
            LayoutInflater.from(activity).inflate(R.layout.add_liquor_popup,null)
        } else {
            LayoutInflater.from(activity).inflate(R.layout.add_ingredient_popup,null)
        }

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

        unitSpinner.adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, createViewModel.unit) }

        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        //Setup type-specific UI
        if (type == LIQUOR_KEY) {
            val liquorSpinner = view.findViewById(R.id.liquorSpinner) as Spinner
            liquorSpinner.adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, createViewModel.liquor) }

            addButton.setOnClickListener{
                val liquor = Ingredient(
                        liquorSpinner.selectedItem.toString(),
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                )
                liquorAdapter.addIngredient(liquor)
                popupWindow.dismiss()
                clearAllFocus()
            }
        } else {
            val ingredientEditText = view.findViewById(R.id.ingredientEditText) as EditText

            addButton.setOnClickListener{
                val ingredient = Ingredient(
                        ingredientEditText.text.toString(),
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                )
                ingredientAdapter.addIngredient(ingredient)
                popupWindow.dismiss()
                clearAllFocus()
            }
        }

        popupWindow.showAtLocation(
                addLiquorButton,
                Gravity.CENTER,
                0,
                0
        )
    }

    private fun clearAllFocus() {
        drinkNameEditText.clearFocus()
        drinkInstructionsEditText.clearFocus()
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}