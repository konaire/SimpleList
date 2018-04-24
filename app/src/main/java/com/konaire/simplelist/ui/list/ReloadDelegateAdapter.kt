package com.konaire.simplelist.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.konaire.simplelist.R

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class ReloadDelegateAdapter(
    private val listener: OnItemSelectedListener<ViewType>
): DelegateAdapter<ViewType> {
    class ReloadViewHolder(
        rootView: View,
        private val listener: OnItemSelectedListener<ViewType>
    ): RecyclerView.ViewHolder(rootView) {
        fun bind(item: ViewType) {
            val button = itemView.findViewById<View>(R.id.button)
            button.setOnClickListener { listener.onItemSelected(item, button) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder =
        ReloadViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_reload, parent, false), listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) =
        (holder as ReloadViewHolder).bind(item)
}