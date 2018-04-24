package com.konaire.simplelist.models

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
data class RepoResponse(
    val repos: List<Repo>,
    val next: Int?
)

data class Repo(
    val id: Long
)