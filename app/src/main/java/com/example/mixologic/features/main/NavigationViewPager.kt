package com.example.mixologic.features.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.mixologic.features.create.CreateFragment
import com.example.mixologic.features.favourite.FavouriteFragment
import com.example.mixologic.features.pantry.PantryFragment
import com.example.mixologic.features.profile.ProfileFragment
import com.example.mixologic.features.search.SearchFragment

class NavigationViewPager(fragmentManager: FragmentManager, behaviour: Int): FragmentStatePagerAdapter(fragmentManager, behaviour) {

    override fun getCount() = 5

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragment.newInstance()
            1 -> FavouriteFragment.newInstance()
            2 -> CreateFragment.newInstance()
            3 -> PantryFragment.newInstance()
            4 -> ProfileFragment.newInstance()
            else -> SearchFragment.newInstance()
        }
    }
}