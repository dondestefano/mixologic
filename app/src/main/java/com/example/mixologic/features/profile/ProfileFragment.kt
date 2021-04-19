package com.example.mixologic.features.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Recipe
import com.example.mixologic.features.recipe.RecipeActivity
import com.example.mixologic.features.search.DrinkAdapter

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var profileRecipesRecyclerView: RecyclerView

    private lateinit var profileTitle: TextView
    private lateinit var pantryInfo: TextView
    private lateinit var recipeInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileTitle = view.findViewById(R.id.usernameTextView)
        pantryInfo = view.findViewById(R.id.pantryInfoTextView)
        recipeInfo = view.findViewById(R.id.recipeInfoTextView)
        profileRecipesRecyclerView = view.findViewById(R.id.profileRecipesRecyclerView)

        initRecyclerView()
        observeViewModel()

        profileTitle.text = profileViewModel.username
        profileViewModel.fetchUsersRecipes()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(profileRecipesRecyclerView.context)
        profileRecipesRecyclerView.layoutManager = layoutManager

        drinkAdapter = DrinkAdapter()
        drinkAdapter.onClickListener = {
            if (it != null) {
                goToDrinkActivity(it)
            }
        }
        profileRecipesRecyclerView.adapter = drinkAdapter
    }

    private fun observeViewModel() {
        profileViewModel.recipeState.observe(viewLifecycleOwner, Observer {
            when (it) {
                FetchState.SUCCESS -> {
                    drinkAdapter.updateItemsToList(profileViewModel.userRecipes)
                }
            }
        })
    }

    private fun goToDrinkActivity(recipe: Recipe) {
        val recipeIntent = Intent(activity, RecipeActivity::class.java)
        recipeIntent.putExtra("recipe", recipe)
        startActivity(recipeIntent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}