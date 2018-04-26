package com.konaire.simplelist.di.repos

import com.konaire.simplelist.ui.repos.RepoListFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 25/04/2018.
 */
@Module
interface RepoFragmentInjector {
    @ContributesAndroidInjector(modules = [RepoListModule::class])
    fun provideRepoListFragment(): RepoListFragment
}