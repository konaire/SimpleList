package com.konaire.simplelist.ui.repos.adapters

import com.konaire.simplelist.ui.list.ListItemType
import com.konaire.simplelist.ui.list.OnItemSelectedListener
import com.konaire.simplelist.ui.list.PaginationAdapter
import com.konaire.simplelist.ui.list.ProgressDelegateAdapter
import com.konaire.simplelist.ui.list.ReloadDelegateAdapter
import com.konaire.simplelist.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class RepoAdapter(
    listener: OnItemSelectedListener<ViewType>
): PaginationAdapter(listener) {
    init {
        delegateAdapters[ListItemType.REPO.ordinal] = RepoDelegateAdapter(listener)
        delegateAdapters[ListItemType.PROGRESS.ordinal] = ProgressDelegateAdapter()
        delegateAdapters[ListItemType.RELOAD.ordinal] = ReloadDelegateAdapter(listener)
    }
}