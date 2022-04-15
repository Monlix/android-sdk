package com.monlixv2.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.ui.fragments.AdsFragment
import com.monlixv2.ui.fragments.OffersFragment
import com.monlixv2.ui.fragments.SurveysFragment
import com.monlixv2.viewmodels.GroupedResponse

const val SURVEY_FRAGMENT = 0
const val OFFER_FRAGMENT = 1
const val AD_FRAGMENT = 2

class PagerAdapter(fa: AppCompatActivity?) :
    FragmentStateAdapter(fa!!) {

    private var data: GroupedResponse? = null
    private var renderedFragments = mutableSetOf<Number>()
    private var filteredCampaigns: ArrayList<Campaign> = ArrayList()

    fun setupData(passedData: GroupedResponse) {
        this.data = passedData
    }

    override fun createFragment(position: Int): Fragment {
        if (!renderedFragments.contains(SURVEY_FRAGMENT) && data?.mergedSurveys?.size!! > 0) {
            renderedFragments.add(SURVEY_FRAGMENT)
            return SurveysFragment.newInstance(data?.mergedSurveys!!)
        }
        if (!renderedFragments.contains(OFFER_FRAGMENT) && data?.campaigns?.size!! > 0) {
            renderedFragments.add(OFFER_FRAGMENT)
            return OffersFragment.newInstance(data?.campaigns!!)
        }
        if (!renderedFragments.contains(AD_FRAGMENT) && data?.offers?.ads?.size!! > 0) {
            renderedFragments.add(AD_FRAGMENT)
            return AdsFragment.newInstance(data?.offers?.ads!!)
        }
        return Fragment()
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        if (data!!.mergedSurveys!!.size > 0) {
            itemCount += 1
        }
        if (data!!.campaigns!!.size > 0) {
            itemCount += 1
        }
        if (data!!.offers!!.ads.size > 0) {
            itemCount += 1
        }
        return itemCount
    }
}
