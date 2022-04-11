package com.monlixv2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

object Constants {

    const val ALL_ACTIVITY = "All activity"
    const val IN_PROGRESS = "In progress"
    const val CREDITED = "Credited"
    const val REJECTED = "Rejected"
    const val CLICKED = "Clicked"
    const val PENDING = "Pending"

    const val ALL_ACTIVITY_QUERY_PARAM = ""
    const val IN_PROGRESS_QUERY_PARAM = "inProgress"
    const val CREDITED_QUERY_PARAM = "credited"
    const val REJECTED_QUERY_PARAM = "cancelled"
    const val CLICKED_QUERY_PARAM = "clicked"
    const val PENDING_QUERY_PARAM = "pending"

    val TRANSACTION_FILTER_LIST =
        arrayOf(ALL_ACTIVITY, IN_PROGRESS, CREDITED, REJECTED, CLICKED, PENDING)

    val TRANSACTION_FILTER_QUERY_PARAM = mapOf<String, String>(
        ALL_ACTIVITY to ALL_ACTIVITY_QUERY_PARAM,
        IN_PROGRESS to IN_PROGRESS_QUERY_PARAM,
        CREDITED to CREDITED_QUERY_PARAM,
        REJECTED to REJECTED_QUERY_PARAM,
        CLICKED to CLICKED_QUERY_PARAM,
        PENDING to PENDING_QUERY_PARAM,
    )

    val TRANSACTION_ITEM_STATUS_DRAWABLE = mapOf<String, Int>(
        IN_PROGRESS_QUERY_PARAM to R.drawable.transaction_status_in_progress,
        CREDITED_QUERY_PARAM to R.drawable.transaction_status_completed,
        REJECTED_QUERY_PARAM to R.drawable.transaction_status_in_rejected,
        CLICKED_QUERY_PARAM to R.drawable.transaction_status_in_clicked,
        PENDING_QUERY_PARAM to R.drawable.transaction_status_in_pending,
    )

    val TRANSACTION_ITEM_STATUS_TEXT_COLOR = mapOf<String, Int>(
        IN_PROGRESS_QUERY_PARAM to R.color.blue,
        CREDITED_QUERY_PARAM to R.color.greenV3,
        REJECTED_QUERY_PARAM to R.color.redV2,
        CLICKED_QUERY_PARAM to R.color.grayV5,
        PENDING_QUERY_PARAM to R.color.orangeV1,
    )

    const val ALL_OFFERS = "All offers"
    const val ANDROID = "Android"
    const val IOS_CAMPAIGN_PARAM = "ios"
    const val ALL_CAMPAIGN_PARAM = "all"
    const val ANDROID_CAMPAIGN_PARAM = "android"


    val OFFER_FILTER_LIST =
        arrayOf(ALL_OFFERS, ANDROID)


    inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }

    val campaignCrComparator = Comparator<Campaign> { a, b ->
        (a.cr - b.cr).roundToInt()
    }

    val campaignHighToLowPayoutComparator = Comparator<Campaign> { a, b ->
        (b.payout.toDouble() - a.payout.toDouble()).roundToInt()
    }
    val campaignLowToHighPayoutComparator = Comparator<Campaign> { a, b ->
        (a.payout.toDouble() - b.payout.toDouble()).roundToInt()
    }
    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    val campaignDateComparator = Comparator<Campaign> { a, b ->
        val date1 = formatter.parse(a.createdAt)
        val date2 = formatter.parse(b.createdAt)

        if(date2!! > date1) 1 else 0
    }


}
