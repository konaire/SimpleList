package com.konaire.simplelist.ui.repos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.konaire.simplelist.R
import com.konaire.simplelist.presenters.repos.RepoListPresenter
import com.konaire.simplelist.ui.BaseFragment
import com.konaire.simplelist.ui.BaseView

import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface RepoListView: BaseView {
    fun showData()
}

class RepoListFragment: BaseFragment(), RepoListView {
    @Inject lateinit var presenter: RepoListPresenter

    companion object {
        private const val TAG = "REPO_LIST"

        fun create() = RepoListFragment()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_repo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.getRepos()
    }

    override fun getTitle(): String = getString(R.string.repo_list)

    override fun getFragmentTag(): String = TAG

    override fun isRoot(): Boolean = true

    override fun showData() {
        android.util.Log.i("111222333", "Hello")
    }
}