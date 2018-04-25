package com.konaire.simplelist.di.network

import com.google.gson.GsonBuilder

import com.konaire.simplelist.BuildConfig
import com.konaire.simplelist.network.Api
import com.konaire.simplelist.network.DelegatingSSLSocketFactory
import com.konaire.simplelist.util.Constants

import dagger.Module
import dagger.Provides

import io.reactivex.schedulers.Schedulers

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.security.KeyStore
import java.util.concurrent.TimeUnit

import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideSSLSocketFactory(): SSLSocketFactory = DelegatingSSLSocketFactory()

    @Singleton
    @Provides
    fun provideTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

        trustManagerFactory.init(null as KeyStore?)

        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + trustManagers.toString())
        }

        return trustManagers[0] as X509TrustManager
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)

        val loggingLevel = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = loggingLevel

        builder.sslSocketFactory(sslSocketFactory, trustManager)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        val builder = Retrofit.Builder().client(client)
        builder.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        builder.baseUrl(Constants.BASE_URL)

        return builder.build()
    }

    @Provides
    fun provideApi(
        retrofit: Retrofit
    ): Api {
        return retrofit.create(Api::class.java)
    }
}