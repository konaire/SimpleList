package com.konaire.simplelist.ui.list

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class PaginationAdapter(
    listener: OnItemClickedListener<ViewType>?
): BaseAdapter<ViewType>(listener) {
    var hasBottomLoader: Boolean = false

    override fun reinit(data: MutableList<ViewType>) {
        if (hasBottomLoader) {
            data.add(object: ViewType {
                override fun getViewType(): Int = ListItemType.PROGRESS.ordinal
            })
        }

        super.reinit(data)
    }

    fun addData(data: MutableList<ViewType>) {
        if (hasBottomLoader) {
            items.addAll(items.lastIndex, data)
        } else {
            items.removeAt(items.lastIndex)
            items.addAll(data)
        }

        notifyDataSetChanged()
    }

    fun showReloadItem() {
        val lastIndex = items.lastIndex
        items[lastIndex] = object: ViewType {
            override fun getViewType(): Int = ListItemType.RELOAD.ordinal
        }

        notifyItemChanged(lastIndex)
    }

    fun hideReloadItem() {
        val lastIndex = items.lastIndex
        items[lastIndex] = object: ViewType {
            override fun getViewType(): Int = ListItemType.PROGRESS.ordinal
        }

        notifyItemChanged(lastIndex)
    }
}