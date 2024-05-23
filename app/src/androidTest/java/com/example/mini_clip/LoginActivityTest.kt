package com.example.mini_clip

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)
    private lateinit var idlingResource: SimpleIdlingResource

    @Before
    fun setUp() {
        Intents.init()
        idlingResource = SimpleIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        Intents.release()
    }

    @Test
    fun testLoginFormValidation() {
        // Type an invalid email and verify error
        onView(withId(R.id.email_input)).perform(typeText("invalidemail"), closeSoftKeyboard())
        onView(withId(R.id.submit_btn)).perform(click())
        onView(withId(R.id.email_input)).check(matches(UiUtil.hasErrorText("Email not valid")))

        // Type a short password and verify error
        onView(withId(R.id.email_input)).perform(replaceText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.submit_btn)).perform(click())
        onView(withId(R.id.password_input)).check(matches(UiUtil.hasErrorText("Minimum 6 character")))
    }

    @Test
    fun testSuccessfulLogin() {
        // Set the idling resource to busy


        // Type valid information
        onView(withId(R.id.email_input)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText("password"), closeSoftKeyboard())

        // Click on login button
        onView(withId(R.id.submit_btn)).perform(click())

        // Check if progress bar is displayed and login button is hidden
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.submit_btn)).check(matches(not(isDisplayed())))

    }

    @Test
    fun testNavigationToSignup() {
        // Click on "Don't have an account" text
        onView(withId(R.id.go_to_signup_btn)).perform(click())

        // Verify the intent to navigate to SignupActivity
        Intents.intended(hasComponent(SignupActivity::class.java.name))
    }
}
