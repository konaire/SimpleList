package com.konaire.simplelist.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.konaire.simplelist.R

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class ProgressDelegateAdapter: DelegateAdapter<ViewType> {
    class ProgressViewHolder(
        rootView: View
    ): RecyclerView.ViewHolder(rootView) {
        fun bind() {
            // do nothing
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder =
        ProgressViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_progress, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) =
        (holder as ProgressViewHolder).bind()
}