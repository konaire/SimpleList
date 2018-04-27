package com.konaire.simplelist.di.repos

import com.konaire.simplelist.di.scopes.ActivityScope
import com.konaire.simplelist.util.Navigation

import dagger.Module
import dagger.Provides

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
@Module
class MockRepoActivityModule {
    @ActivityScope
    @Provides
    fun provideNavigation(): Navigation = Navigation()
}