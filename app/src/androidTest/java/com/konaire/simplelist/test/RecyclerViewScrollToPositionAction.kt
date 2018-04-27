package com.konaire.simplelist.test

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import android.view.View

import org.hamcrest.Matcher
import org.hamcrest.Matchers.*

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
class RecyclerViewScrollToPositionAction(
    private val position: Int
): ViewAction {
    companion object {
        fun smoothScrollTo(position: Int): RecyclerViewScrollToPositionAction = RecyclerViewScrollToPositionAction(position)
    }

    override fun getDescription(): String = "Smooth scroll RecyclerView to $position position"

    override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun perform(uiController: UiController?, view: View?) {
        val recyclerView = view as RecyclerView
        recyclerView.smoothScrollToPosition(position)
    }
}