package com.konaire.simplelist.ui

import com.konaire.simplelist.ui.list.OnItemSelectedListener
import com.konaire.simplelist.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface BaseView {
    fun showError(message: String)
    fun showError(messageResource: Int)
}

interface BaseListView: BaseView, OnItemSelectedListener<ViewType> {
    fun showProgress()
    fun hideProgress()

    fun showData(data: MutableList<ViewType>)
    fun addData(data: MutableList<ViewType>)
    fun setNextItem(next: Int?)
    fun showReloadItem()
}