package com.konaire.simplelist.di

import android.content.Context

import com.konaire.simplelist.App
import com.konaire.simplelist.di.repos.RepoActivityModule
import com.konaire.simplelist.ui.repos.RepoActivity

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
interface AppModule {
    @Binds
    @Singleton
    fun provideContext(app: App): Context

    @ContributesAndroidInjector(modules = [RepoActivityModule::class])
    fun provideRepoActivity(): RepoActivity
}