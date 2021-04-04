package com.example.mixologic.features.popup

import android.app.Activity
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.mixologic.R
import com.example.mixologic.data.Ingredient
import com.example.mixologic.features.create.CreateAdapter
import com.example.mixologic.features.create.LIQUOR_KEY
import com.example.mixologic.managers.LiquorManager

class IngredientPopup {
    fun showPopUp(type: String, adapter: CreateAdapter, activity: Activity, clearFocus: Unit, locationView: View, liquors: List<String>?) {
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


        unitSpinner.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, LiquorManager.getUnits())

        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        //Setup type-specific UI
        if (type == LIQUOR_KEY) {
            val liquorSpinner = view.findViewById(R.id.liquorSpinner) as Spinner
            liquorSpinner.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, liquors ?: LiquorManager.getLiquors())

            addButton.setOnClickListener{
                if (!unitEditText.text.isNullOrBlank()) {
                    val liquor = Ingredient(
                        liquorSpinner.selectedItem.toString(),
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                    )
                    adapter.addIngredient(liquor)
                    popupWindow.dismiss()
                    clearFocus
                }
            }
        } else {
            val ingredientEditText = view.findViewById(R.id.ingredientEditText) as EditText

            addButton.setOnClickListener{
                if (!ingredientEditText.text.isNullOrBlank() && !unitEditText.text.isNullOrBlank()) {
                    val ingredient = Ingredient(
                        ingredientEditText.text.toString(),
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                    )
                    adapter.addIngredient(ingredient)
                    popupWindow.dismiss()
                    clearFocus
                }
            }
        }

        popupWindow.showAtLocation(
            locationView,
            Gravity.CENTER,
            0,
            0
        )
    }
}