package com.example.mixologic.features.recipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mixologic.R
import com.example.mixologic.data.Like
import com.example.mixologic.data.Recipe
import com.example.mixologic.databinding.ActivityRecipeBinding
import com.example.mixologic.features.create.IngredientAdapter
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.LikeManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val RECIPE_KEY = "recipe"

class RecipeActivity: AppCompatActivity() {
    private lateinit var binding : ActivityRecipeBinding
    private val recipe by lazy { intent.getParcelableExtra<Recipe>(RECIPE_KEY) }

    private lateinit var liquorAdapter: IngredientAdapter
    private lateinit var ingredientAdapter: IngredientAdapter

    var hasUserLiked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipe)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe)
        binding.backButton.setOnClickListener{
            finish()
        }

        binding.likeButton.setOnClickListener{
            userLiked()
        }

        recipe?.likes?.let {
            hasUserLiked = LikeManager.hasUserLiked(it, AccountManager.getUser().uid)
        }

        initRecyclerViews()
    }

    private fun initRecyclerViews() {

        if(!recipe?.liquors.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(this )
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            binding.liquorsRecyclerView.layoutManager = layoutManager
            liquorAdapter = IngredientAdapter(true, editable = false)
            binding.liquorsRecyclerView.adapter = liquorAdapter

            recipe?.liquors?.let { liquorAdapter.updateItemsToList(it) }
        }

        if(!recipe?.ingredients.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(this )
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            binding.ingredientsRecyclerView.layoutManager = layoutManager
            ingredientAdapter = IngredientAdapter(false, editable = false)
            binding.ingredientsRecyclerView.adapter = ingredientAdapter

            recipe?.ingredients?.let { ingredientAdapter.updateItemsToList(it) }
        }
    }

    private fun userLiked() {
        GlobalScope.launch {
            recipe?.let { LikeManager.handleOnLiked(it, AccountManager.getUser().uid) }

            hasUserLiked = !hasUserLiked
            val like = Like(AccountManager.getUser().uid)

            if (recipe?.likes == null) {
                recipe?.likes = mutableListOf(like)
            } else {
                if(hasUserLiked) {
                    recipe!!.likes?.add(like)
                } else {
                    recipe!!.likes?.remove(like)
                }
            }

            binding.recipe = recipe
        }


    }

    override fun onResume() {
        super.onResume()
        binding.recipe = recipe
        binding.executePendingBindings()
    }
}