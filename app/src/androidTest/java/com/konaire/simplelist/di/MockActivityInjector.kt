package com.konaire.simplelist.di

import com.konaire.simplelist.di.repos.MockRepoActivityModule
import com.konaire.simplelist.di.repos.RepoFragmentInjector
import com.konaire.simplelist.di.scopes.ActivityScope
import com.konaire.simplelist.ui.repos.RepoActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
@Module
interface MockActivityInjector {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MockRepoActivityModule::class, RepoFragmentInjector::class])
    fun provideRepoActivity(): RepoActivity
}