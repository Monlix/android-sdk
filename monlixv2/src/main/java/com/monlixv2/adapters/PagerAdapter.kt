package com.monlixv2.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.monlixv2.ui.fragments.AdsFragment
import com.monlixv2.ui.fragments.OffersFragment
import com.monlixv2.ui.fragments.SurveysFragment
import com.monlixv2.viewmodels.GroupedResponse


const val SURVEY_POSITION = 0
const val OFFER_POSITION = 1
const val AD_POSITION = 2

class PagerAdapter(fa: AppCompatActivity?) :
    FragmentStateAdapter(fa!!) {

    private var data: GroupedResponse? = null

    fun setupData(passedData: GroupedResponse) {
        this.data = passedData
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment =
            when (position) {
                SURVEY_POSITION -> SurveysFragment.newInstance(data!!.mergedSurveys!!)
                OFFER_POSITION -> OffersFragment.newInstance(data!!.campaigns!!)
                else
                -> AdsFragment.newInstance(data!!.offers!!.ads)
            }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}
