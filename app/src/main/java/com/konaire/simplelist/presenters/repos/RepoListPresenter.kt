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
    fun getRepos()
}

class RepoListPresenterImpl @Inject constructor(
    private val interactor: RepoListInteractor,
    private val view: RepoListView
): RepoListPresenter {
    private var getReposSubscription: Disposable? = null

    override fun stopSubscriptions() {
        getReposSubscription?.dispose()
    }

    override fun getRepos() {
        getReposSubscription = interactor.getRepos()
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { view.showData() }, { error -> view.showError(R.string.network_error) }
            )
    }
}