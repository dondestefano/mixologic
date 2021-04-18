package com.example.mixologic.features.recipe

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mixologic.R
import com.example.mixologic.data.Recipe
import com.example.mixologic.databinding.ActivityRecipeBinding

class RecipeActivity: AppCompatActivity() {
    private lateinit var binding : ActivityRecipeBinding
    private val recipe by lazy { intent.getParcelableExtra<Recipe>("recipe") }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipe)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe)
        binding.backButton.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recipe = recipe
    }
}