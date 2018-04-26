package com.konaire.simplelist.di

import com.konaire.simplelist.di.repos.RepoActivityModule
import com.konaire.simplelist.di.repos.RepoFragmentInjector
import com.konaire.simplelist.di.scopes.ActivityScope
import com.konaire.simplelist.ui.repos.RepoActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 25/04/2018.
 */
@Module
interface ActivityInjector {
    @ActivityScope
    @ContributesAndroidInjector(modules = [RepoActivityModule::class, RepoFragmentInjector::class])
    fun provideRepoActivity(): RepoActivity
}