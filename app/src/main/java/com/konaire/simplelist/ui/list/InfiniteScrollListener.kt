package com.konaire.simplelist.ui.list

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class InfiniteScrollListener(
    private val visibleThreshold: Int,
    private val layoutManager: LinearLayoutManager,
    private val getMoreItems: () -> Unit
): RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var wasLoaded = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy != 0) {
            val totalItemCount = layoutManager.itemCount
            val visibleItemCount = recyclerView.childCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (wasLoaded && totalItemCount != previousTotal) {
                wasLoaded = false
            }

            if (!wasLoaded && (totalItemCount - (firstVisibleItem + visibleItemCount)) <= visibleThreshold) {
                previousTotal = totalItemCount
                wasLoaded = true
                getMoreItems()
            }
        }
    }
}