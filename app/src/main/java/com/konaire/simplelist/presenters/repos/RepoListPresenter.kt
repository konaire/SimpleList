package com.konaire.simplelist.presenters.repos

import com.konaire.simplelist.R
import com.konaire.simplelist.interactors.repos.RepoListInteractor
import com.konaire.simplelist.presenters.BasePresenter
import com.konaire.simplelist.ui.repos.RepoListView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import java.util.concurrent.TimeUnit

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface RepoListPresenter: BasePresenter {
    fun getFirstRepos()
    fun getMoreRepos(page: Int)
}

class RepoListPresenterImpl @Inject constructor(
    private val interactor: RepoListInteractor,
    private val view: RepoListView
): RepoListPresenter {
    private var getFirstReposSubscription: Disposable? = null
    private var getMoreReposSubscription: Disposable? = null

    override fun stopSubscriptions() {
        getFirstReposSubscription?.dispose()
        getMoreReposSubscription?.dispose()
    }

    override fun getFirstRepos() {
        view.showProgress()
        getFirstReposSubscription =
            interactor.getReposRemotely(null)
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .onErrorResumeNext(interactor.getReposLocally(null).doAfterSuccess({
                    view.showError(R.string.network_error_switch_to_db)
                })).doOnDispose { view.hideProgress() }
                .subscribe(
                    { response ->
                        view.hideProgress()
                        view.setNextItem(response.next)
                        view.showData(ArrayList(response.repos))
                    }, {
                        view.hideProgress()
                        view.showError(R.string.network_error)
                    }
                )
    }

    override fun getMoreRepos(page: Int) {
        getMoreReposSubscription =
            interactor.getReposRemotely(page)
                .onErrorResumeNext(interactor.getReposLocally(page).doAfterSuccess({
                    view.showError(R.string.network_error_switch_to_db)
                })).doOnDispose { view.showReloadItem() }
                .subscribe(
                    { response ->
                        view.setNextItem(response.next)
                        view.addData(ArrayList(response.repos))
                    }, { view.showReloadItem() }
                )
    }
}