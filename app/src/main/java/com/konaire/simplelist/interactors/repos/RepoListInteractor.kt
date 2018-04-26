package com.konaire.simplelist.interactors.repos

import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.models.RepoResponse
import com.konaire.simplelist.network.Api
import com.konaire.simplelist.util.Constants
import com.konaire.simplelist.util.DatabaseSelectException
import com.konaire.simplelist.util.getNextPage
import com.konaire.simplelist.util.testSafeFlowable

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

import io.realm.Realm

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface RepoListInteractor {
    fun getReposRemotely(
        page: Int?, uiScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Single<RepoResponse>

    fun getReposLocally(page: Int?): Single<RepoResponse>
}

class RepoListInteractorImpl @Inject constructor(
    private val api: Api,
    private val realm: Realm
): RepoListInteractor {
    override fun getReposRemotely(page: Int?, uiScheduler: Scheduler): Single<RepoResponse> =
        api.getJakeWhartonRepos(page).map { result ->
            val response = result.response()
            val error = result.error()
            if (error != null) {
                throw error
            }

            RepoResponse(response?.body() ?: ArrayList(), result.getNextPage())
        }.observeOn(uiScheduler)
        .doAfterSuccess { response ->
            realm.executeTransaction { r ->
                r.insertOrUpdate(response.repos.onEach { it.apply { fullName = fullName.toLowerCase() } })
            }
        }

    override fun getReposLocally(page: Int?): Single<RepoResponse> {
        return realm.where(Repo::class.java).findAllAsync().sort("fullName")
            .testSafeFlowable().filter { it.isLoaded }
            .map { result ->
                val nextPage = page ?: 1
                val currentPage = nextPage - 1
                val from = currentPage * Constants.ITEMS_PER_PAGE
                val to = nextPage * Constants.ITEMS_PER_PAGE

                if (from < result.size) {
                    val dbList = result.subList(from, if (to < result.size) to else result.size)
                    val list = realm.copyFromRealm(dbList)
                    RepoResponse(list, nextPage + 1)
                } else {
                    throw DatabaseSelectException("Cache doesn't hold this data")
                }
            }.firstOrError()
    }
}