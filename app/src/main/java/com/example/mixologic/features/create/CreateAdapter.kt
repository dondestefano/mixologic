package com.example.mixologic.features.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.Ingredient

class CreateAdapter(private val isLiquorAdapter: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contents = mutableListOf<Ingredient>()
    var onClickListener: (Ingredient?) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isLiquorAdapter) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_add_liquor, parent,false) as View
            AddIngredientViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_add_ingredient, parent,false) as View
            AddIngredientViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AddIngredientViewHolder).bind(contents[position])
    }

    override fun getItemCount() = contents.size

    fun addIngredient(ingredient: Ingredient) {
        contents.add(ingredient)
    }

    fun getIngredients(): List<Ingredient>{
        return contents
    }

    inner class AddIngredientViewHolder(private var view: View): RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.ingredientTextView)
        private val removeButton = view.findViewById<ImageView>(R.id.removeIngredientButton)

        fun bind(ingredient: Ingredient) {
            text.text = "${ingredient.name} - ${ingredient.amount} ${ingredient.unit}"
            removeButton.setOnClickListener{
                contents.remove(contents[adapterPosition])
                notifyDataSetChanged()
            }
            view.setOnClickListener {
                onClickListener(contents[adapterPosition])
            }
        }
    }
}