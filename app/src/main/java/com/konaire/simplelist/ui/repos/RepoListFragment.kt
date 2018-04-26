package com.konaire.simplelist.ui.repos

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.konaire.simplelist.R
import com.konaire.simplelist.models.Repo
import com.konaire.simplelist.presenters.repos.RepoListPresenter
import com.konaire.simplelist.ui.BaseFragment
import com.konaire.simplelist.ui.BaseListView
import com.konaire.simplelist.ui.list.DividerDecoration
import com.konaire.simplelist.ui.list.InfiniteScrollListener
import com.konaire.simplelist.ui.list.ViewType
import com.konaire.simplelist.ui.repos.adapters.RepoAdapter
import com.konaire.simplelist.util.Constants

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_repo_list.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface RepoListView: BaseListView

class RepoListFragment: BaseFragment(), RepoListView {
    @Inject lateinit var presenter: RepoListPresenter

    private val adapter: RepoAdapter by lazy {
        presenter.getFirstRepos()
        RepoAdapter(this)
    }

    companion object {
        private const val TAG = "REPO_LIST"
        private const val KEY_NEXT_PAGE = "KEY_NEXT_PAGE"

        fun create(): RepoListFragment {
            val fragment = RepoListFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private fun getNextPage(): Int = arguments?.getInt(KEY_NEXT_PAGE, 0) ?: 0

    private fun setNextPage(page: Int) = arguments?.putInt(KEY_NEXT_PAGE, page)

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onStop() {
        super.onStop()
        presenter.stopSubscriptions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_repo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(activity)
        val scrollListener = InfiniteScrollListener(
            Constants.VISIBLE_ITEM_THRESHOLD,
            layoutManager,
            {
                if (adapter.hasBottomProgress && !adapter.hasBottomReloader) {
                    presenter.getMoreRepos(getNextPage())
                }
            }
        )

        list.adapter = adapter
        list.layoutManager = layoutManager
        list.addOnScrollListener(scrollListener)
        list.addItemDecoration(DividerDecoration(activity!!))
        swipe.setOnRefreshListener { presenter.getFirstRepos() }
        emptyView?.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        list.clearOnScrollListeners()
        super.onDestroyView()
    }

    override fun getTitle(): String = getString(R.string.repo_list_title)

    override fun getFragmentTag(): String = TAG

    override fun isRoot(): Boolean = true

    override fun showProgress() {
        swipe?.isRefreshing = true
    }

    override fun hideProgress() {
        swipe?.isRefreshing = false
    }

    override fun showData(data: MutableList<ViewType>) {
        if (data.isNotEmpty()) {
            adapter.reinit(data)
            emptyView?.visibility = View.GONE
        } else {
            emptyView?.visibility = View.VISIBLE
        }
    }

    override fun addData(data: MutableList<ViewType>) = adapter.addData(data)

    override fun setNextItem(next: Int?) {
        val hasBottomProgress = (next != null && next > 0)
        adapter.hasBottomProgress = hasBottomProgress
        setNextPage(next ?: 0)
    }

    override fun showReloadItem() = adapter.showReloadItem()

    override fun onItemClicked(item: ViewType, view: View) {
        if (item is Repo) {
            Log.i(TAG, item.id.toString())
        } else { // reload item
            adapter.hideReloadItem()
            presenter.getMoreRepos(getNextPage())
        }
    }
}