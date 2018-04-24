package com.konaire.simplelist.ui.list

import android.view.View

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface OnItemSelectedListener<in T> where T: ViewType {
    fun onItemSelected(item: T, view: View)
}