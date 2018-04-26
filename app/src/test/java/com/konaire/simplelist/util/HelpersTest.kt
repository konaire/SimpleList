package com.konaire.simplelist.util

import com.konaire.simplelist.models.Repo

import okhttp3.Headers

import org.junit.Assert.*
import org.junit.Test

import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

/**
 * Created by Evgeny Eliseyev on 26/04/2018.
 */
class HelpersTest {
    @Test
    fun isApiGivesCorrectDate() {
        assertEquals("23.04.2018", "2018-04-23T09:53:30Z".formatAsDate())
    }

    @Test
    fun isTimezoneMatters() {
        assertEquals("24.04.2018", "2018-04-23T22:53:30Z".formatAsDate())
    }

    @Test
    fun isAnotherGeneralFormattedDateWorks() {
        assertEquals("23.04.2018", "2018-04-23T22:53:30GMT+02:00".formatAsDate())
    }

    @Test
    fun isApiGivesCorrectPage() {
        val headers = Headers.of("Link", "<https://api.github.com/user/66577/repos?per_page=15&sort=full_name&page=2>; rel=\"next\", <https://api.github.com/user/66577/repos?per_page=15&sort=full_name&page=7>; rel=\"last\"")
        val response = Response.success(ArrayList<Repo>(), headers)
        val result = Result.response(response)

        assertEquals(2, result.getNextPage())
    }

    @Test
    fun isLastPageProcessesCorrectly() {
        val headers = Headers.of("Link", "<https://api.github.com/user/66577/repos?page=6&per_page=15&sort=full_name>; rel=\"prev\", <https://api.github.com/user/66577/repos?page=1&per_page=15&sort=full_name>; rel=\"first\"")
        val response = Response.success(ArrayList<Repo>(), headers)
        val result = Result.response(response)

        assertEquals(null, result.getNextPage())
    }

    @Test
    fun isResponseWithoutLinkHeaderProcessesCorrectly() {
        val headers = Headers.of("Mock", "mocked")
        val response = Response.success(ArrayList<Repo>(), headers)
        val result = Result.response(response)

        assertEquals(null, result.getNextPage())
    }
}