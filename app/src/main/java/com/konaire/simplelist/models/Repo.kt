package com.konaire.simplelist.models

import com.google.gson.annotations.SerializedName

import com.konaire.simplelist.ui.list.ListItemType
import com.konaire.simplelist.ui.list.ViewType
import com.konaire.simplelist.util.formatAsDate

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
data class Repo(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_at") private val createdAt: String,
    @SerializedName("updated_at") private val updatedAt: String
): ViewType {
    override fun getViewType(): Int = ListItemType.REPO.ordinal

    fun formattedCreatedDate(): String = createdAt.formatAsDate()

    fun formattedUpdatedDate(): String = updatedAt.formatAsDate()
}

data class RepoResponse(
    val repos: MutableList<Repo>,
    val next: Int?
)