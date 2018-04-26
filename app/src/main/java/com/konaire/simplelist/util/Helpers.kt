package com.konaire.simplelist.util

import android.content.Context
import android.widget.Toast

import io.reactivex.Flowable

import io.realm.RealmResults

import retrofit2.adapter.rxjava2.Result

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class DatabaseSelectException(message: String): RuntimeException(message)

fun String.formatAsDate(): String {
    val inf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
    val outf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return outf.format(inf.parse(this.replace("Z", "UTC")))
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun <T> Result<T>.getNextPage(): Int? {
    val linkHeader = response()?.headers()?.get("Link")
    val nextPageLink = linkHeader?.split(",")?.firstOrNull { it.contains("next") }
    val nextPageParameters = nextPageLink?.split(";")?.firstOrNull { it.contains(Constants.BASE_URL) }?.substringAfter("?")
    return nextPageParameters?.split("&")?.firstOrNull { it.contains("^page".toRegex()) }?.replace("\\D".toRegex(), "")?.toInt()
}

fun <T> RealmResults<T>.testSafeFlowable(): Flowable<RealmResults<T>> {
    var flowable = asFlowable()

    // check that result went as mock
    if (flowable == null && isLoaded && !isValid) {
        flowable = Flowable.just(this)
    }

    return flowable
}