package com.konaire.simplelist.test

import android.support.test.espresso.IdlingResource
import android.support.v7.widget.RecyclerView

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
class RecyclerViewScrollingIdlingResource(
    private val recyclerView: RecyclerView
): IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = RecyclerViewScrollingIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {
        val isIdle = !recyclerView.layoutManager.isSmoothScrolling
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }

        return isIdle
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = resourceCallback
    }
}