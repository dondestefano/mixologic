package com.example.mixologic.features.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Recipe
import com.example.mixologic.features.recipe.RECIPE_KEY
import com.example.mixologic.features.recipe.RecipeActivity

class SearchFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var searchRecipesRecyclerView: RecyclerView
    private lateinit var searchBarEditText: EditText
    private lateinit var organizeButton: ImageView

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
        searchBarEditText = view.findViewById(R.id.searchBarEditText)
        organizeButton = view.findViewById(R.id.organizeButton)

        initRecyclerView()
        initSearchBar()
        observeViewModel()

        searchViewModel.fetchRecipes()

        organizeButton.setOnClickListener {
            showSortPopup(organizeButton)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(searchRecipesRecyclerView.context)
        searchRecipesRecyclerView.layoutManager = layoutManager

        drinkAdapter = DrinkAdapter()
        drinkAdapter.onClickListener = {
            if (it != null) {
                goToRecipeActivity(it)
            }
        }
        searchRecipesRecyclerView.adapter = drinkAdapter
    }

    private fun initSearchBar() {
        searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchViewModel.filterBySearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun observeViewModel() {
        searchViewModel.recipeState.observe(viewLifecycleOwner, Observer {
            when (it) {
                FetchState.SUCCESS -> {
                    drinkAdapter.updateItemsToList(searchViewModel.recipes)
                }

                FetchState.SORTED -> {
                    searchViewModel.sortList(searchViewModel.sorted)
                }

                FetchState.FILTERED -> {
                    searchViewModel.filterByPantry()
                }

                FetchState.FILTERED_SORTED -> {
                    searchViewModel.filterByPantry()
                    searchViewModel.sortList(searchViewModel.sorted)
                }

                else -> {}
            }
        })
    }

    private fun showSortPopup(v: View) {
        val popup = PopupMenu(activity, v)
        popup.setOnMenuItemClickListener(this)

        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sort, popup.menu)

        popup.show()
    }


    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_pantry -> {
                searchViewModel.setFiltered()
                searchViewModel.filterByPantry()
                true
            }
            R.id.sort_fave -> {
                searchViewModel.sortList(SORT_FAVOURITE)
                true
            }

            R.id.sort_az -> {
                searchViewModel.sortList(SORT_AZ)
                true
            }

            R.id.sort_za -> {
                searchViewModel.sortList(SORT_ZA)
                true
            }

            else -> false
        }
    }

    private fun goToRecipeActivity(recipe: Recipe) {
        val recipeIntent = Intent(activity, RecipeActivity::class.java)
        recipeIntent.putExtra(RECIPE_KEY, recipe)
        startActivity(recipeIntent)
        activity?.overridePendingTransition(R.anim.slide_left, R.anim.stay)
    }
}
