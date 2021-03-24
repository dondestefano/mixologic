package com.example.mixologic.features.search

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

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var searchRecipesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRecipesRecyclerView = view.findViewById(R.id.searchRecipesRecyclerView)

        initRecyclerView()
        observeViewModel()

        searchViewModel.fetchRecipes()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(searchRecipesRecyclerView.context)
        searchRecipesRecyclerView.layoutManager = layoutManager

        drinkAdapter = DrinkAdapter()
        searchRecipesRecyclerView.adapter = drinkAdapter
    }

    private fun observeViewModel() {
        searchViewModel.recipeState.observe(viewLifecycleOwner, Observer {
            when (it) {
                RecipeState.SUCCESS -> {
                    drinkAdapter.updateItemsToList(searchViewModel.recipes)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}