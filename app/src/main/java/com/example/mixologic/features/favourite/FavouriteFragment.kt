package com.example.mixologic.features.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.RecipeState
import com.example.mixologic.features.search.DrinkAdapter
import com.example.mixologic.features.search.SearchViewModel

class FavouriteFragment : Fragment() {
    private val favouriteViewModel: FavouriteViewModel by viewModels()

    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var favouriteRecipesRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteRecipesRecyclerView = view.findViewById(R.id.favouriteRecipesRecyclerView)

        initRecyclerView()
        observeViewModel()

        favouriteViewModel.fetchLikedRecipes()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(favouriteRecipesRecyclerView.context)
        favouriteRecipesRecyclerView.layoutManager = layoutManager

        drinkAdapter = DrinkAdapter()
        favouriteRecipesRecyclerView.adapter = drinkAdapter
    }

    private fun observeViewModel() {
        favouriteViewModel.recipeState.observe(viewLifecycleOwner, Observer {
            when (it) {
                RecipeState.SUCCESS -> {
                    drinkAdapter.updateItemsToList(favouriteViewModel.recipes)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}