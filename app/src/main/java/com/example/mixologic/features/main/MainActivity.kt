package com.example.mixologic.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mixologic.R
import com.example.mixologic.application.MixologicApplication
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navigationViewPager : ViewPager
    private lateinit var menu : BottomNavigationView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                navigationViewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_favourites -> {
                navigationViewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_create -> {
                navigationViewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_pantry -> {
                navigationViewPager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                navigationViewPager.currentItem = 4
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationViewPager = findViewById(R.id.navigation_viewpager)
        menu = findViewById(R.id.navigationBar)

        setupViewPager()

        menu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun setupViewPager() {
        navigationViewPager.offscreenPageLimit = 5
        navigationViewPager.adapter = NavigationViewPager(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

        // Add listener to update menu when swiping between fragments.
        navigationViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            // Required override functions
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> menu.selectedItemId = R.id.nav_home
                    1 -> menu.selectedItemId = R.id.nav_favourites
                    2 -> menu.selectedItemId = R.id.nav_create
                    3 -> menu.selectedItemId = R.id.nav_pantry
                    4 -> menu.selectedItemId = R.id.nav_profile
                }
            }
        })
    }

    override fun onDestroy() {
        GlobalScope.launch {
            (application as MixologicApplication).dataRepository.deleteDataFromCache()
        }
        super.onDestroy()
    }

}