package com.konaire.simplelist.models

import com.google.gson.annotations.SerializedName

import com.konaire.simplelist.ui.list.ListItemType
import com.konaire.simplelist.ui.list.ViewType
import com.konaire.simplelist.util.formatAsDate

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
open class Repo(
    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @Required
    @SerializedName("full_name")
    var fullName: String,

    @Required
    @SerializedName("name")
    var name: String,

    @SerializedName("description")
    var description: String?,

    @Required
    @SerializedName("created_at")
    private var createdAt: String,

    @Required
    @SerializedName("updated_at")
    private var updatedAt: String
): RealmObject(), ViewType {
    constructor(): this(0, "", "", null, "", "")

    override fun getViewType(): Int = ListItemType.REPO.ordinal

    fun formattedCreatedDate(): String = createdAt.formatAsDate()

    fun formattedUpdatedDate(): String = updatedAt.formatAsDate()
}

data class RepoResponse(
    val repos: MutableList<Repo>,
    val next: Int?
)