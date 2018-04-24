package com.konaire.simplelist.di.repos

import com.konaire.simplelist.ui.repos.RepoListFragment
import com.konaire.simplelist.util.Navigation
import com.konaire.simplelist.util.NavigationImpl

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
interface RepoActivityModule {
    @Binds
    fun provideNavigation(navigation: NavigationImpl): Navigation

    @ContributesAndroidInjector(modules = [RepoListModule::class])
    fun provideRepoListFragment(): RepoListFragment
}