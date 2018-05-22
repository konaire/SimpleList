package com.konaire.simplelist.ui.list

import com.konaire.simplelist.util.Constants

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class PaginationAdapter(
    listener: OnItemClickedListener<ViewType>
): BaseAdapter<ViewType>(listener) {
    var hasBottomProgress: Boolean = false
    var hasBottomReloader: Boolean = false

    override fun reinit(data: MutableList<ViewType>) {
        val isOnePageWithProgress = (items.size - 1 == Constants.ITEMS_PER_PAGE)
        if (isOnePageWithProgress && hasBottomReloader) {
            data.add(getReloadItem())
        } else if (hasBottomProgress) {
            data.add(getProgressItem())
            hasBottomReloader = false
        }

        super.reinit(data)
    }

    fun addData(data: MutableList<ViewType>) {
        if (hasBottomProgress) {
            items.addAll(items.lastIndex, data)
        } else {
            items.removeAt(items.lastIndex)
            items.addAll(data)
        }

        notifyDataSetChanged()
    }

    fun showReloadItem() {
        val lastIndex = items.lastIndex
        items[lastIndex] = getReloadItem()
        notifyItemChanged(lastIndex)
        hasBottomReloader = true
    }

    fun hideReloadItem() {
        val lastIndex = items.lastIndex
        items[lastIndex] = getProgressItem()
        notifyItemChanged(lastIndex)
        hasBottomReloader = false
    }

    private fun getProgressItem(): ViewType = object: ViewType {
        override fun getViewType(): Int = ListItemType.PROGRESS.ordinal
    }

    private fun getReloadItem(): ViewType = object: ViewType {
        override fun getViewType(): Int = ListItemType.RELOAD.ordinal
    }
}