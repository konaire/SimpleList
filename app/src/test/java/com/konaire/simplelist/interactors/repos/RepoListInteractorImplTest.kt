package com.konaire.simplelist.interactors.repos

import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.models.RepoResponse
import com.konaire.simplelist.network.Api
import com.konaire.simplelist.util.Constants
import com.konaire.simplelist.util.DatabaseSelectException

import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subscribers.TestSubscriber

import io.realm.Realm
import io.realm.RealmResults

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

import retrofit2.adapter.rxjava2.Result

import java.util.concurrent.TimeUnit

/**
 * Created by Evgeny Eliseyev on 26/04/2018.
 */
@RunWith(MockitoJUnitRunner.Silent::class)
class RepoListInteractorImplTest {
    @Mock private lateinit var api: Api
    private lateinit var realm: Realm

    @InjectMocks private lateinit var scheduler: TestScheduler
    private lateinit var interactor: RepoListInteractorImpl

    @Before
    fun setUp() {
        realm = mock(Realm::class.java, Mockito.RETURNS_DEEP_STUBS)
        interactor = RepoListInteractorImpl(api, realm)
    }

    @Test
    fun isRequestCrashesAfterError() {
        val result = Result.error<MutableList<Repo>>(NullPointerException())
        `when`(api.getJakeWhartonRepos(any(), anyInt(), anyString())).thenReturn(Single.just(result))

        val subscriber = TestSubscriber.create<RepoResponse>()
        interactor.getReposRemotely(null, scheduler).toFlowable().subscribe(subscriber)
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        scheduler.triggerActions()

        subscriber.assertError { it is NullPointerException }
    }

    @Test
    fun isDbSendsErrorForEmptyData() {
        val result = mockRealmResults(ArrayList())
        val subscriber = TestSubscriber.create<RepoResponse>()
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(result)
        interactor.getReposLocally(null).toFlowable().subscribe(subscriber)

        subscriber.assertError { it is DatabaseSelectException }
    }

    @Test
    fun isDbLoadsPartialData() {
        val result = mockRealmResults(listOf(
            Repo(), Repo(), Repo()
        ).toMutableList())
        val subscriber = TestSubscriber.create<RepoResponse>()
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(result)
        interactor.getReposLocally(null).toFlowable().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue { it.repos.size == 3 }
    }

    @Test
    fun isDbLoadsFullData() {
        val result = mockRealmResults(listOf(
            Repo(), Repo(), Repo(), Repo(), Repo(),
            Repo(), Repo(), Repo(), Repo(), Repo(),
            Repo(), Repo(), Repo(), Repo(), Repo()
        ).toMutableList())
        val subscriber = TestSubscriber.create<RepoResponse>()
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(result)
        interactor.getReposLocally(null).toFlowable().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue { it.repos.size == Constants.ITEMS_PER_PAGE }
    }

    @Test
    fun isDbLoadsDataWithPagination() {
        val result = mockRealmResults(listOf(
            Repo(), Repo(), Repo(), Repo(), Repo(), Repo(),
            Repo(), Repo(), Repo(), Repo(), Repo(), Repo(),
            Repo(), Repo(), Repo(), Repo(), Repo(), Repo()
        ).toMutableList())
        val subscriber = TestSubscriber.create<RepoResponse>()
        `when`(realm.where(Repo::class.java).findAllAsync().sort("fullName")).thenReturn(result)
        interactor.getReposLocally(2).toFlowable().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue { response -> response.repos.size < Constants.ITEMS_PER_PAGE && response.next == 3 }
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