package com.example.mixologic.features.recipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mixologic.R
import com.example.mixologic.data.Recipe
import com.example.mixologic.databinding.ActivityRecipeBinding
import com.example.mixologic.features.create.CreateAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class RecipeActivity: AppCompatActivity() {
    private lateinit var binding : ActivityRecipeBinding
    private val recipe by lazy { intent.getParcelableExtra<Recipe>("recipe") }

    private lateinit var liquorAdapter: CreateAdapter
    private lateinit var ingredientAdapter: CreateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipe)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe)
        binding.backButton.setOnClickListener{
            finish()
        }

        initRecyclerViews()
    }

    private fun initRecyclerViews() {

        if(!recipe?.liquors.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(this )
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            binding.liquorsRecyclerView.layoutManager = layoutManager
            liquorAdapter = CreateAdapter(true)
            binding.liquorsRecyclerView.adapter = liquorAdapter

            recipe?.liquors?.let { liquorAdapter.updateItemsToList(it) }
        }

        if(!recipe?.ingredients.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(this )
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START

            binding.ingredientsRecyclerView.layoutManager = layoutManager
            ingredientAdapter = CreateAdapter(false)
            binding.ingredientsRecyclerView.adapter = ingredientAdapter

            recipe?.ingredients?.let { ingredientAdapter.updateItemsToList(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recipe = recipe
    }
}