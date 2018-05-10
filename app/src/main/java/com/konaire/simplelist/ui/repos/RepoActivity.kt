package com.konaire.simplelist.ui.repos

import android.os.Bundle

import com.konaire.simplelist.R
import com.konaire.simplelist.ui.BaseActivity

import io.realm.Realm

import kotlinx.android.synthetic.main.activity_repo.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
class RepoActivity: BaseActivity() {
    @Inject lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            navigation.showFragment(this, RepoListFragment.create(), true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}