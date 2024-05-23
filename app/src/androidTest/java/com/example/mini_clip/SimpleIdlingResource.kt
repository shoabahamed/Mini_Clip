package com.example.mini_clip

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class SimpleIdlingResource : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    // Boolean to track if the resource is idle or not
    private val isIdleNow = AtomicBoolean(true)

    override fun getName(): String {
        return this::class.java.name
    }

    override fun isIdleNow(): Boolean {
        return isIdleNow.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        this.isIdleNow.set(isIdleNow)
        if (isIdleNow && callback != null) {
            callback!!.onTransitionToIdle()
        }
    }
}
