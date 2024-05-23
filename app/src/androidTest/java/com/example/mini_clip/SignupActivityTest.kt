package com.example.mini_clip

import android.content.Intent
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
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SignupActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(SignupActivity::class.java)
    private lateinit var idlingResource: SimpleIdlingResource

    @Before
    fun setUp() {
        Intents.init()
        idlingResource = SimpleIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testSignupFormValidation() {
        // Type an invalid email and verify error
        onView(withId(R.id.email_input)).perform(typeText("invalidemail"), closeSoftKeyboard())
        onView(withId(R.id.submit_btn)).perform(click())
        onView(withId(R.id.email_input)).check(matches(UiUtil.hasErrorText("Email not valid")))

        // Type a short password and verify error
        onView(withId(R.id.email_input)).perform(replaceText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.submit_btn)).perform(click())
        onView(withId(R.id.password_input)).check(matches(UiUtil.hasErrorText("Minimum 6 character")))

        // Type mismatched passwords and verify error
        onView(withId(R.id.password_input)).perform(replaceText("password"), closeSoftKeyboard())
        onView(withId(R.id.confirm_password_input)).perform(typeText("differentpassword"), closeSoftKeyboard())
        onView(withId(R.id.submit_btn)).perform(click())
        onView(withId(R.id.confirm_password_input)).check(matches(UiUtil.hasErrorText("Password not matched")))
    }

    @Test
    fun testSuccessfulSignup() {
        // Type valid information
        onView(withId(R.id.email_input)).perform(typeText("test2@example.com"), closeSoftKeyboard())
        onView(withId(R.id.username_input)).perform(typeText("testuser"), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.confirm_password_input)).perform(typeText("password"), closeSoftKeyboard())

        // Click on signup button
        onView(withId(R.id.submit_btn)).perform(click())

        // Check if progress bar is displayed and signup button is hidden
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.submit_btn)).check(matches(not(isDisplayed())))



        // Optionally, verify if the account creation toast is shown
        // onView(withText("Account created successfully"))
        //     .inRoot(withDecorView(not(is(activityScenarioRule.scenario.window.decorView))))
        //     .check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToLogin() {
        // Click on "Already have an account" text
        onView(withId(R.id.go_to_login_btn)).perform(click())

        // Verify the intent to navigate to LoginActivity
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        Intents.release()
    }
}
