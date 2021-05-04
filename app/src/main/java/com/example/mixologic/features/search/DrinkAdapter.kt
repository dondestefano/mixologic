package com.example.mixologic.features.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.data.Recipe
import com.example.mixologic.databinding.ViewDrinkListBinding
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.LikeManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DrinkAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipes = listOf<Recipe>()

    var onClickListener: (Recipe?) -> Unit = {}

    fun updateItemsToList(list : List<Recipe>) {
        recipes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DrinkViewHolder(
            ViewDrinkListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipe = recipes[position]

        (holder as DrinkViewHolder).bind(recipe)
    }

    override fun getItemCount() = recipes.size


    inner class DrinkViewHolder(private val binding: ViewDrinkListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipeItem: Recipe) {
            binding.apply {
                recipe = recipeItem
                executePendingBindings()
            }

            binding.likeButton.setOnClickListener{
               GlobalScope.launch {
                    LikeManager.handleOnLiked(recipeItem, AccountManager.getUser().uid)
                }
            }

            binding.drinkCardContainer.setOnClickListener {
                onClickListener(recipeItem)
            }
        }
    }
}