package com.konaire.simplelist.network

import io.reactivex.Single

import retrofit2.http.GET

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface Api {
    @GET("repos")
    fun getRepos(): Single<Unit>
}