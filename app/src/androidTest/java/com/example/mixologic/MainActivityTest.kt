package com.example.mixologic

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.mixologic.features.login.LoginActivity
import org.junit.Before
import org.junit.Test

class MainActivityTest {
    private val testAccount = "tom@tom.tom"
    private val testPassword = "tom123"

    @Before
    fun setUp() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(ViewMatchers.withId(R.id.emailLoginEditText)).perform(ViewActions.replaceText(testAccount), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.passwordLoginEditText)).perform(ViewActions.replaceText(testPassword), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(1500L)
    }

    @Test
    fun test_isNavigationBarVisible() {
        onView(ViewMatchers.withId(R.id.navigationBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.nav_home)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.nav_favourites)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.nav_create)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.nav_pantry)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.nav_profile)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isSeachPageVisible() {
        onView(ViewMatchers.withId(R.id.nav_home)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.searchTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isFavouritePageVisible() {
        onView(ViewMatchers.withId(R.id.nav_favourites)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.favouriteTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isCreatePageVisible() {
        onView(ViewMatchers.withId(R.id.nav_create)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.createTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isPantryPageVisible() {
        onView(ViewMatchers.withId(R.id.nav_pantry)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.pantryTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_isProfilePageVisible() {
        onView(ViewMatchers.withId(R.id.nav_profile)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.profileTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}