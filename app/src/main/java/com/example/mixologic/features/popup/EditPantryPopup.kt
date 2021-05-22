package com.example.mixologic.features.popup

import android.app.Activity
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.mixologic.R
import com.example.mixologic.data.Ingredient
import com.example.mixologic.features.pantry.PantryViewModel
import com.example.mixologic.managers.LiquorManager

class EditPantryPopup(ingredientToEdit: Ingredient) {
    val ingredient = ingredientToEdit

    fun showPopup(activity: Activity, pantryViewModel: PantryViewModel, locationView: View) {
        val view = LayoutInflater.from(activity).inflate(R.layout.edit_ingredient_popup,null)

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
        val titleText = view.findViewById(R.id.editLiquorTitle) as TextView
        val unitSpinner = view.findViewById(R.id.unitSpinner) as Spinner
        val unitEditText = view.findViewById(R.id.unitEditText) as EditText
        val cancelButton = view.findViewById(R.id.cancelButton) as Button
        val saveButton = view.findViewById(R.id.saveButton) as Button

        titleText.text = String.format(activity.resources.getString(R.string.edit_liquor), ingredient.name)

        unitSpinner.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, LiquorManager.getUnits())
        ingredient.unit?.let { unitSpinner.setSelection(valueToPosition(it)) }

        unitEditText.setText(ingredient.amount.toString())

        // Setup buttons
        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        saveButton.setOnClickListener{
            if (!unitEditText.text.isNullOrBlank()) {
                val liquor = Ingredient(
                        ingredient.name,
                        unitEditText.text.toString().toInt(),
                        unitSpinner.selectedItem.toString()
                )
                if (liquor != ingredient) {
                    pantryViewModel.savePantryItem(liquor)
                }
                popupWindow.dismiss()
            }
        }

        popupWindow.showAtLocation(
                locationView,
                Gravity.CENTER,
                0,
                0
        )
    }

    private fun valueToPosition(value: String): Int {
        LiquorManager.getUnits().forEachIndexed {pos, unit ->
            if (unit == value) {
                return pos
            }
        }
        return 0
    }
}