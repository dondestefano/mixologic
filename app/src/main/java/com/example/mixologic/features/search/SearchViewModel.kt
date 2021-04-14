package com.example.mixologic.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.FilterManager
import com.example.mixologic.managers.FirebaseManager
import com.example.mixologic.managers.LiquorManager

const val NOT_SORTED = "not_sorted"
const val SORT_FAVOURITE = "sort_favourite"
const val SORT_AZ = "sort_az"
const val SORT_ZA = "sort_za"


class SearchViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()
    private var filtered = false
    var sorted = NOT_SORTED
    private lateinit var filterManger: FilterManager

    fun fetchRecipes() {
        recipeState.value = FetchState.LOADING
        FirebaseManager.getRecipeDatabase().addSnapshotListener{ value, error ->
            if (value != null) {
                val allRecipes = value.toObjects(Recipe::class.java)
                filterManger = FilterManager(allRecipes)
                recipes = allRecipes

                setListStatus()

            } else {
                recipeState.value = FetchState.ERROR
            }
        }
    }

    private fun setListStatus() {
        recipeState.value =
                if (filtered && sorted != NOT_SORTED) {
                    FetchState.FILTERED_SORTED
                }
                else if (filtered) {
                    FetchState.FILTERED
                }
                else if (sorted != NOT_SORTED) {
                    FetchState.SORTED
                }
                else {
                    FetchState.SUCCESS
                }
    }

    fun filterByPantry() {
        recipes = if (filtered) {
            filterManger.filterByPantry(LiquorManager.getPantry())
        } else {
            filterManger.originalList
        }

        recipeState.value = FetchState.SUCCESS
    }

    fun filterBySearch(keyword: String) {
        recipeState.value = FetchState.LOADING

        recipes = filterManger.filterByKeyword(keyword)

        recipeState.value = FetchState.SUCCESS
    }

    fun sortList(sortType: String) {
        recipeState.value = FetchState.LOADING

        when(sortType) {
            SORT_FAVOURITE -> {
                sorted = SORT_FAVOURITE
                recipes = recipes.sortedByDescending { recipe -> recipe.likes?.size }
            }
            SORT_AZ -> {
                sorted = SORT_AZ
                recipes = recipes.sortedBy { recipe -> recipe.name }
            }
            SORT_ZA -> {
                sorted = SORT_ZA
                recipes = recipes.sortedByDescending { recipe -> recipe.name }
            }
        }

        recipeState.value = FetchState.SUCCESS
    }

    fun setFiltered() {
        recipeState.value = FetchState.LOADING
        filtered = !filtered
    }
}