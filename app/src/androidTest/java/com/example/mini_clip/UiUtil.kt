package com.example.mini_clip

import android.view.View
import android.widget.EditText
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object UiUtil {
    fun hasErrorText(expectedError: String): Matcher<View> {
        return object : BoundedMatcher<View, EditText>(EditText::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has error text: $expectedError")
            }

            override fun matchesSafely(editText: EditText): Boolean {
                return expectedError == editText.error?.toString()
            }
        }
    }
}
