package com.konaire.simplelist.ui.repos

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView

import com.konaire.simplelist.MockApp
import com.konaire.simplelist.R
import com.konaire.simplelist.di.DaggerMockAppComponent
import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.network.Api
import com.konaire.simplelist.test.RecyclerViewItemCountAssertion
import com.konaire.simplelist.test.RecyclerViewScrollToPositionAction
import com.konaire.simplelist.test.RecyclerViewScrollingIdlingResource
import com.konaire.simplelist.util.Constants

import io.reactivex.Single

import io.realm.Realm
import io.realm.RealmResults

import okhttp3.Headers

import org.hamcrest.Matchers

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.*

import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 26/04/2018.
 */
@RunWith(AndroidJUnit4::class)
class RepoActivityTest {
    @Rule @JvmField val activityRule: ActivityTestRule<RepoActivity> = ActivityTestRule(RepoActivity::class.java, true, false)

    @Inject lateinit var api: Api
    @Inject lateinit var realm: Realm

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MockApp
        (app.component as DaggerMockAppComponent).inject(this)
    }

    @Test
    fun checkWhenEverythingIsFine() {
        val dbResult = mockRealmResults(listOf(
            Repo(), Repo(), Repo()
        ).toMutableList())

        val networkResult = Result.response(Response.success(listOf(
            Repo(), Repo(), Repo(), Repo(), Repo(), Repo()
        ).toMutableList()))

        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(dbResult)
        `when`(api.getJakeWhartonRepos(any(), anyInt(), anyString())).thenReturn(Single.just(networkResult))

        activityRule.launchActivity(Intent())
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .check(RecyclerViewItemCountAssertion.withItemCount(6))
    }

    @Test
    fun checkWhenNetworkCrashes() {
        val dbResult = mockRealmResults(listOf(
            Repo(), Repo(), Repo()
        ).toMutableList())
        val networkResult = Result.error<MutableList<Repo>>(NullPointerException())
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(dbResult)
        `when`(api.getJakeWhartonRepos(any(), anyInt(), anyString())).thenReturn(Single.just(networkResult))

        activityRule.launchActivity(Intent())
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .check(RecyclerViewItemCountAssertion.withItemCount(3 + 1)) // because we have progress item in the bottom
    }

    @Test
    fun checkWhenBothSourcesAreEmpty() {
        val dbResult = mockRealmResults(ArrayList())
        val networkResult = Result.error<MutableList<Repo>>(NullPointerException())
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(dbResult)
        `when`(api.getJakeWhartonRepos(any(), anyInt(), anyString())).thenReturn(Single.just(networkResult))

        activityRule.launchActivity(Intent())
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .check(RecyclerViewItemCountAssertion.withItemCount(0))
    }

    @Test
    fun checkThatScrollWorksCorrectly() {
        val dbResult = mockRealmResults(ArrayList())
        val networkResult = Result.response(Response.success(listOf(
                Repo(), Repo(), Repo(), Repo(), Repo(),
                Repo(), Repo(), Repo(), Repo(), Repo(),
                Repo(), Repo(), Repo(), Repo(), Repo()
            ).toMutableList(),
            Headers.of("Link", "<https://api.github.com/user/66577/repos?per_page=15&sort=full_name&page=2>; rel=\"next\"")
        ))

        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(dbResult)
        `when`(api.getJakeWhartonRepos(any(), anyInt(), anyString())).thenReturn(Single.just(networkResult))

        activityRule.launchActivity(Intent())
        val list = activityRule.activity.findViewById(R.id.list) as RecyclerView
        val idlingResource = RecyclerViewScrollingIdlingResource(list)
        IdlingRegistry.getInstance().register(idlingResource)
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .perform(RecyclerViewScrollToPositionAction.smoothScrollTo(Constants.ITEMS_PER_PAGE - Constants.VISIBLE_ITEM_THRESHOLD))
            .check(RecyclerViewItemCountAssertion.withItemCount(Matchers.greaterThan(Constants.ITEMS_PER_PAGE + 1))) // because we have progress item in the bottom

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockRealmResults(list: MutableList<Repo>): RealmResults<Repo> {
        val result = mock(RealmResults::class.java) as RealmResults<Repo>

        `when`(realm.copyFromRealm(anyIterableOf(Repo::class.java))).then { answer -> answer.arguments[0] as Iterable<Repo> }
        `when`(result.subList(anyInt(), anyInt())).then { answer -> list.subList(answer.arguments[0] as Int, answer.arguments[1] as Int) }
        `when`(result.size).thenReturn(list.size)
        `when`(result.isLoaded).thenReturn(true)
        `when`(result.isValid).thenReturn(false)
        return result
    }
}