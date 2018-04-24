package com.konaire.simplelist.ui

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface BaseView {
    fun showError(message: String)
    fun showError(messageResource: Int)
}