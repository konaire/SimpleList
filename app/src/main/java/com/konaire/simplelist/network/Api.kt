package com.konaire.simplelist.network

import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.util.Constants

import io.reactivex.Single

import retrofit2.adapter.rxjava2.Result

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface Api {
    @GET("users/JakeWharton/repos")
    fun getJakeWhartonRepos(@Query("page") page: Int?, @Query("per_page") pageSize: Int = Constants.ITEMS_PER_PAGE): Single<Result<MutableList<Repo>>>
}