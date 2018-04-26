package com.konaire.simplelist.di.repos

import com.konaire.simplelist.di.scopes.ActivityScope
import com.konaire.simplelist.util.Navigation

import dagger.Module
import dagger.Provides

import io.realm.Realm

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
class RepoActivityModule {
    @ActivityScope
    @Provides
    fun provideNavigation(): Navigation = Navigation()

    @ActivityScope
    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}