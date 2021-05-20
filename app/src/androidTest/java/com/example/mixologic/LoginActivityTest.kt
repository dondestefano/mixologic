package com.example.mixologic

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.mixologic.features.login.LoginActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {
    val testAccount = "tom@tom.tom"
    val testPassword = "tom123"

    @Before
    fun setUp() {
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun test_isLoginScreenVisible() {
        onView(withId(R.id.emailLoginEditText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.passwordLoginEditText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.loginButton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.signupTextView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_signupButton() {
        onView(withId(R.id.signupTextView)).perform(ViewActions.click())
        onView(withId(R.id.signupContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        pressBack()

        onView(withId(R.id.emailLoginEditText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.passwordLoginEditText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.loginButton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.signupTextView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_login() {
        onView(withId(R.id.emailLoginEditText)).perform(ViewActions.replaceText(testAccount), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.passwordLoginEditText)).perform(ViewActions.replaceText(testPassword), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(1500L)

        onView(withId(R.id.navigationBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}