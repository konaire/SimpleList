package com.konaire.simplelist.ui.repos.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.konaire.simplelist.R
import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.ui.list.DelegateAdapter
import com.konaire.simplelist.ui.list.OnItemClickedListener
import com.konaire.simplelist.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class RepoDelegateAdapter(
    private val listener: OnItemClickedListener<Repo>
): DelegateAdapter<ViewType> {
    class RepoViewHolder(
        rootView: View,
        private val listener: OnItemClickedListener<Repo>
    ): RecyclerView.ViewHolder(rootView) {
        fun bind(repo: Repo)  = with(itemView) {
            val name = findViewById<TextView>(R.id.name)
            val description = findViewById<TextView>(R.id.description)
            val createdDate = findViewById<TextView>(R.id.createdDate)
            val updatedDate = findViewById<TextView>(R.id.updatedDate)

            name.text = repo.name
            description.text = repo.description
            createdDate.text = context.getString(R.string.repo_list_item_created_date, repo.formattedCreatedDate())
            updatedDate.text = context.getString(R.string.repo_list_item_updated_date, repo.formattedUpdatedDate())

            setOnClickListener { listener.onItemClicked(repo, this) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder =
        RepoViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_repo, parent, false), listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) =
        (holder as RepoViewHolder).bind(item as Repo)
}