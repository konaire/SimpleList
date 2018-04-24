package com.konaire.simplelist.interactors.repos

import com.konaire.simplelist.models.RepoResponse
import com.konaire.simplelist.network.Api
import com.konaire.simplelist.util.getNextPage

import io.reactivex.Single

import retrofit2.HttpException

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface RepoListInteractor {
    fun getRepos(page: Int?): Single<RepoResponse>
}

class RepoListInteractorImpl @Inject constructor(
    private val api: Api
): RepoListInteractor {
    override fun getRepos(page: Int?): Single<RepoResponse> = api.getRepos(page).map { result ->
        val response = result.response()
        if (response?.isSuccessful == false) {
            throw HttpException(response)
        }

        RepoResponse(response?.body() ?: ArrayList(), result.getNextPage())
    }
}