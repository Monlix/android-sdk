package com.monlixv2.util

import android.content.Context
import android.content.SharedPreferences

object Constants {

    const val ALL_ACTIVITY = "All activity"
    const val IN_PROGRESS = "In progress"
    const val CREDITED = "Credited"
    const val REJECTED = "Rejected"
    const val CLICKED = "Clicked"
    const val PENDING = "Pending"

    val TRANSACTION_FILTER_LIST = arrayOf(ALL_ACTIVITY, IN_PROGRESS, CREDITED, REJECTED, CLICKED, PENDING)
}
