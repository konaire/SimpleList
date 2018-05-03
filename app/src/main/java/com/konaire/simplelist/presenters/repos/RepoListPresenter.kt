package com.konaire.simplelist.presenters.repos

import com.konaire.simplelist.R
import com.konaire.simplelist.interactors.repos.RepoListInteractor
import com.konaire.simplelist.presenters.BasePresenter
import com.konaire.simplelist.ui.repos.RepoListView

import io.reactivex.disposables.CompositeDisposable

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
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stopSubscriptions() {
        disposables.clear()
    }

    override fun getFirstRepos() {
        view.showProgress()
        disposables.add(interactor.getReposRemotely(null)
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
        )
    }

    override fun getMoreRepos(page: Int) {
        disposables.add(interactor.getReposRemotely(page)
            .onErrorResumeNext(interactor.getReposLocally(page).doAfterSuccess({
                view.showError(R.string.network_error_switch_to_db)
            })).doOnDispose { view.showReloadItem() }
            .subscribe(
                { response ->
                    view.setNextItem(response.next)
                    view.addData(ArrayList(response.repos))
                }, { view.showReloadItem() }
            )
        )
    }
}