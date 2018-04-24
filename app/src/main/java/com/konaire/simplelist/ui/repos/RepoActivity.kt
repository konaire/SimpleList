package com.konaire.simplelist.ui.repos

import android.os.Bundle
import android.view.MenuItem

import com.konaire.simplelist.R
import com.konaire.simplelist.ui.BaseFragment
import com.konaire.simplelist.util.Navigation

import dagger.android.support.DaggerAppCompatActivity

import kotlinx.android.synthetic.main.activity_repo.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
class RepoActivity: DaggerAppCompatActivity() {
    @Inject lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            navigation.showFragment(this, RepoListFragment.create(), true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigation.closeFragment(this)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as BaseFragment
        if (fragment.defaultBackButtonBehaviour()) {
            super.onBackPressed()
        }
    }
}
