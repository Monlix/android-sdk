package com.monlixv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object Constants {

    const val ALL_ACTIVITY = "All activity"
    const val IN_PROGRESS = "In progress"
    const val CREDITED = "Credited"
    const val REJECTED = "Rejected"
    const val CLICKED = "Clicked"
    const val PENDING = "Pending"

    val TRANSACTION_FILTER_LIST = arrayOf(ALL_ACTIVITY, IN_PROGRESS, CREDITED, REJECTED, CLICKED, PENDING)

    inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }
}
