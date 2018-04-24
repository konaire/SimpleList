package com.konaire.simplelist.util

import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity

import com.konaire.simplelist.R
import com.konaire.simplelist.ui.BaseFragment

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 * Do not use this class directly. It should be created via DI.
 */
interface Navigation {
    fun showFragment(activity: AppCompatActivity?, fragment: BaseFragment, notAddToBackStack: Boolean = false)
    fun closeFragment(activity: AppCompatActivity?)
}

class NavigationImpl @Inject constructor(): Navigation {
    override fun showFragment(activity: AppCompatActivity?, fragment: BaseFragment, notAddToBackStack: Boolean) {
        if (activity == null) {
            return
        }

        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction()
        if (fragment.isRoot()) {
            val previousFragment = manager.findFragmentByTag(fragment.getFragmentTag())

            if (previousFragment == null || !previousFragment.isVisible) {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).commit()
            }
        } else {
            val previousFragment = manager.findFragmentById(R.id.mainContainer)

            if (previousFragment is BaseFragment &&
                previousFragment.getFragmentTag().isNotEmpty() &&
                previousFragment.getFragmentTag() == fragment.getFragmentTag()) {

                return
            }

            if (notAddToBackStack) {
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).commit()
            } else {
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).addToBackStack(null).commit()
            }
        }
    }

    override fun closeFragment(activity: AppCompatActivity?) {
        if (activity == null) {
            return
        }

        val fragment = activity.supportFragmentManager.findFragmentById(R.id.mainContainer)

        if (fragment is BaseFragment) {
            if (fragment.defaultBackArrowBehaviour()) {
                if (fragment.isRoot()) {
                    activity.onBackPressed()
                } else {
                    activity.supportFragmentManager.popBackStack()
                }
            }
        } else {
            activity.onBackPressed()
        }
    }
}