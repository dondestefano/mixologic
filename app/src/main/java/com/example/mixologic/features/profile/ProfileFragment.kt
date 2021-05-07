package com.example.mixologic.features.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Recipe
import com.example.mixologic.features.recipe.RECIPE_KEY
import com.example.mixologic.features.recipe.RecipeActivity
import com.example.mixologic.features.search.DrinkAdapter
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.LiquorManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_profile.*

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var profileRecipesRecyclerView: RecyclerView

    private lateinit var profileTitle: TextView
    private lateinit var pantryInfo: TextView
    private lateinit var recipeInfo: TextView
    private lateinit var saveChangesButton: Button

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
        saveChangesButton = view.findViewById(R.id.saveChangesButton)

        initRecyclerView()
        observeViewModel()
        initButtons()

        profileViewModel.fetchUsersRecipes()
        loadProfile()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(profileRecipesRecyclerView.context)
        profileRecipesRecyclerView.layoutManager = layoutManager

        drinkAdapter = DrinkAdapter()
        drinkAdapter.onClickListener = {
            if (it != null) {
                goToRecipeActivity(it)
            }
        }
        profileRecipesRecyclerView.adapter = drinkAdapter
    }

    private fun initButtons() {
        profilePictureImageView.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        saveChangesButton.setOnClickListener{
            profileViewModel.saveProfilePicture((activity?.application as MixologicApplication))
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
                profileViewModel.imageURL = data.data!!

                Picasso
                    .get()
                    .load(profileViewModel.imageURL)
                    .fit()
                    .centerCrop()
                    .into(profilePictureImageView)

                saveChangesButton.visibility = View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        profileViewModel.recipeState.observe(viewLifecycleOwner, Observer {
            when (it) {
                FetchState.SUCCESS -> {
                    drinkAdapter.updateItemsToList(profileViewModel.userRecipes)
                    loadProfile()
                }
                else -> {}
            }

        })

        profileViewModel.imageState.observe(viewLifecycleOwner, Observer {
            when (it) {
                ImageState.SUCCESS -> {
                    loadProfile()
                    saveChangesButton.visibility = View.GONE
                }
                else -> {}
            }
        })
    }

    private fun loadProfile() {
        profileTitle.text = profileViewModel.username
        recipeInfo.text = String.format(resources.getString(R.string.profile_recipe), profileViewModel.userRecipes.size.toString())
        pantryInfo.text = String.format(resources.getString(R.string.profile_pantry), LiquorManager.getPantry().size.toString())

        if (profileViewModel.imageURL != null) {
            Picasso
                .get()
                .load(profileViewModel.imageURL)
                .fit()
                .centerCrop()
                .into(profilePictureImageView)
        }
        else if (!AccountManager.getUserdata().profileImageURL.isNullOrEmpty()) {
            Picasso
                .get()
                .load(AccountManager.getUserdata().profileImageURL)
                .fit()
                .centerCrop()
                .into(profilePictureImageView)
        }
    }

    private fun goToRecipeActivity(recipe: Recipe) {
        val recipeIntent = Intent(activity, RecipeActivity::class.java)
        recipeIntent.putExtra(RECIPE_KEY, recipe)
        startActivity(recipeIntent)
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}