package com.example.mixologic.features.create

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R

const val LIQUOR_KEY = "LIQUOR_KEY"
const val INGREDIENT_KEY = "INGREDIENT_KEY"

class CreateFragment : Fragment() {
    private lateinit var liquorAdapter: CreateAdapter
    private lateinit var ingredientAdapter: CreateAdapter

    private lateinit var addLiquorButton: ImageView
    private lateinit var addIngredientButton: ImageView

    private var liquorRecyclerView: RecyclerView? = null
    private var ingredientRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addIngredientButton = view.findViewById(R.id.addIngredientButton)
        addLiquorButton = view.findViewById(R.id.addLiquorButton)

        initButtons()
        initRecyclerViews()
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

        liquorAdapter.addDummyData()
        ingredientAdapter.addDummyData()
    }

    private fun initButtons() {
        addLiquorButton.setOnClickListener{
            showPopUp(LIQUOR_KEY)
        }

        addIngredientButton.setOnClickListener{
            showPopUp(INGREDIENT_KEY)
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }

        val unitSpinner = view.findViewById(R.id.unitSpinner) as Spinner
        val unitEditText = view.findViewById(R.id.unitEditText) as EditText
        val cancelButton = view.findViewById(R.id.cancelButton) as Button
        val addButton = view.findViewById(R.id.addButton) as Button


        val unit = listOf("ml", "cl", "dl", "st")

        unitSpinner.adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, unit) }

        if (type == LIQUOR_KEY) {
            val liquor = listOf("Vodka", "Rum", "Gin", "Whiskey")

            val liquorSpinner = view.findViewById(R.id.liquorSpinner) as Spinner
            liquorSpinner.adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, liquor) }

            unitSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    unitEditText.setText(position.toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        else {
            val ingredientEditText = view.findViewById(R.id.ingredientEditText) as EditText
        }


            cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.isFocusable = true


        popupWindow.showAtLocation(
                addLiquorButton,
                Gravity.CENTER,
                0,
                0
        )
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}