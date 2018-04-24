package com.konaire.simplelist.presenters.repos

import com.konaire.simplelist.R
import com.konaire.simplelist.interactors.repos.RepoListInteractor
import com.konaire.simplelist.presenters.BasePresenter
import com.konaire.simplelist.ui.repos.RepoListView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

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

        getFirstReposSubscription = interactor.getRepos(null)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
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
        getMoreReposSubscription = interactor.getRepos(page)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { response ->
                    view.setNextItem(response.next)
                    view.addData(ArrayList(response.repos))
                }, { view.showReloadItem() }
            )
    }
}