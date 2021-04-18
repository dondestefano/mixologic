package com.example.mixologic.features.recipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

class RecipeActivity: AppCompatActivity() {
    private lateinit var binding : ActivityRecipeBinding
    private val recipe by lazy { intent.getParcelableExtra<Recipe>("recipe") }

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
        recipe?.let { LikeManager.handleOnLiked(it, AccountManager.getUser().uid) }

        hasUserLiked = !hasUserLiked

        if(hasUserLiked) {
            val likedIcon = ContextCompat.getDrawable(this, R.drawable.ic_heart_full)
            binding.likeButton.setImageDrawable(likedIcon)

            if(!LikeManager.hasUserLiked(recipe?.likes!!, AccountManager.getUser().uid)) {
                val like = Like(AccountManager.getUser().uid)
                recipe!!.likes?.add(like)
            }
        } else {
            val notLikedIcon = ContextCompat.getDrawable(this, R.drawable.ic_heart_outline)
            binding.likeButton.setImageDrawable(notLikedIcon)

            if(LikeManager.hasUserLiked(recipe?.likes!!, AccountManager.getUser().uid)) {
                val like = Like(AccountManager.getUser().uid)
                recipe!!.likes?.remove(like)
            }
        }

        binding.likeCounterTextView.text = recipe!!.likes?.size.toString()
    }

    override fun onResume() {
        super.onResume()
        binding.recipe = recipe
    }
}