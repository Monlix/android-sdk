package com.monlixv2.util

import androidx.collection.arrayMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

object Constants {

    const val FRAGMENT_NAME_SURVEYS = "Surveys"
    const val FRAGMENT_NAME_OFFERS = "Offers"
    const val FRAGMENT_NAME_ADS = "Ads"

    const val AVAILABLE_FRAGMENTS = "AVAILABLE_FRAGMENTS"

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
    const val CAMPAIGNS_PAYLOAD = "campaigns"
    const val SINGLE_CAMPAIGN_PAYLOAD = "singleCampaign"

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")


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
        (b.payout - a.payout).roundToInt()
    }
    val campaignLowToHighPayoutComparator = Comparator<Campaign> { a, b ->
        (a.payout - b.payout).roundToInt()
    }

    enum class SORT_FILTER {
        RECOMMENDED,
        HIGH_TO_LOW,
        LOW_TO_HIGH,
        NEWEST,
        NONE
    }

    val SORT_IDS_TO_SORT_FILTER = arrayMapOf(
        R.id.sortRecommended to SORT_FILTER.RECOMMENDED,
        R.id.sortHighToLow to SORT_FILTER.HIGH_TO_LOW,
        R.id.sortLowToHigh to SORT_FILTER.LOW_TO_HIGH,
        R.id.sortNewest to SORT_FILTER.NEWEST,
    )

    val SORT_FILTER_TO_ID = arrayMapOf(
        SORT_FILTER.RECOMMENDED to R.id.sortRecommended,
        SORT_FILTER.HIGH_TO_LOW to R.id.sortHighToLow,
        SORT_FILTER.LOW_TO_HIGH to R.id.sortLowToHigh,
        SORT_FILTER.NEWEST to R.id.sortNewest
    )


}
