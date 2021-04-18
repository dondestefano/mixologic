package com.example.mixologic.features.create

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.Recipe
import com.example.mixologic.features.popup.IngredientPopup
import java.util.*

const val LIQUOR_KEY = "LIQUOR_KEY"
const val INGREDIENT_KEY = "INGREDIENT_KEY"

class CreateFragment : Fragment() {
    private val createViewModel: CreateViewModel by viewModels()

    private lateinit var liquorAdapter: IngredientAdapter
    private lateinit var ingredientAdapter: IngredientAdapter

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
            showPopUp(LIQUOR_KEY, liquorAdapter)
        }

        addIngredientButton.setOnClickListener{
            showPopUp(INGREDIENT_KEY, ingredientAdapter)
        }

        submitButton.setOnClickListener() {
            createViewModel.saveRecipe(createRecipe())
        }
    }

    private fun initRecyclerViews() {
        ingredientRecyclerView = view?.findViewById(R.id.ingredientsRecyclerView)
        ingredientRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        ingredientAdapter = IngredientAdapter(false, editable = true)
        ingredientRecyclerView?.adapter = ingredientAdapter

        liquorRecyclerView = view?.findViewById(R.id.liquorsRecyclerView)
        liquorRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        liquorAdapter = IngredientAdapter(true, editable = true)
        liquorRecyclerView?.adapter = liquorAdapter
    }

    private fun createRecipe(): Recipe {

        return Recipe(
            drinkNameEditText.text.toString(),
            drinkInstructionsEditText.text.toString(),
            liquorAdapter.getIngredients(),
            ingredientAdapter.getIngredients(),
            UUID.randomUUID().toString(),
            com.example.mixologic.managers.AccountManager.getUser().uid,
            ""
        )
    }

    private fun showPopUp(type: String, adapter: IngredientAdapter) {
        val ingredientPopup = IngredientPopup()

        activity?.let {
            ingredientPopup.showPopUp(
                type,
                adapter,
                it,
                clearAllFocus(),
                addIngredientButton,
                null
            )
        }
    }

    private fun clearAllFocus() {
        drinkNameEditText.clearFocus()
        drinkInstructionsEditText.clearFocus()
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}