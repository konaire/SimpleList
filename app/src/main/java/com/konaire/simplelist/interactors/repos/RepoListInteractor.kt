package com.konaire.simplelist.interactors.repos

import com.konaire.simplelist.network.Api

import io.reactivex.Single

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface RepoListInteractor {
    fun getRepos(): Single<Unit>
}

class RepoListInteractorImpl @Inject constructor(
    private val api: Api
): RepoListInteractor {
    override fun getRepos(): Single<Unit> = api.getRepos()
}