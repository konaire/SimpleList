package com.konaire.simplelist.di.repos

import com.konaire.simplelist.interactors.repos.RepoListInteractor
import com.konaire.simplelist.interactors.repos.RepoListInteractorImpl
import com.konaire.simplelist.presenters.repos.RepoListPresenter
import com.konaire.simplelist.presenters.repos.RepoListPresenterImpl
import com.konaire.simplelist.ui.repos.RepoListFragment
import com.konaire.simplelist.ui.repos.RepoListView

import dagger.Binds
import dagger.Module

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
interface RepoListModule {
    @Binds
    fun provideRepoListPresenter(presenter: RepoListPresenterImpl): RepoListPresenter

    @Binds
    fun provideRepoListInteractor(interactor: RepoListInteractorImpl): RepoListInteractor

    @Binds
    fun provideRepoListView(fragment: RepoListFragment): RepoListView
}