package com.konaire.simplelist.di

import android.content.Context

import com.konaire.simplelist.MockApp
import com.konaire.simplelist.network.Api

import dagger.Module
import dagger.Provides

import io.realm.Realm

import org.mockito.Mockito

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
@Module
class MockAppModule {
    @Singleton
    @Provides
    fun provideContext(app: MockApp): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideApi(): Api = Mockito.mock(Api::class.java)

    @Singleton
    @Provides
    fun provideRealm(): Realm = Mockito.mock(Realm::class.java, Mockito.RETURNS_DEEP_STUBS)
}