package com.monlixv2.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.monlixv2.ui.fragments.AdsFragment
import com.monlixv2.ui.fragments.OffersFragment
import com.monlixv2.ui.fragments.SurveysFragment

const val SURVEY_FRAGMENT = 0
const val OFFER_FRAGMENT = 1
const val AD_FRAGMENT = 2

class PagerAdapter(fa: AppCompatActivity?) :
    FragmentStateAdapter(fa!!) {

    private var availableFragments: IntArray? = null
    private var renderedFragments = mutableSetOf<Number>()

    fun setupData(passedFragments: IntArray) {
        this.availableFragments = passedFragments
    }

    override fun createFragment(position: Int): Fragment {
        if (!renderedFragments.contains(OFFER_FRAGMENT) && availableFragments!!.contains(
                OFFER_FRAGMENT
            )
        ) {
            renderedFragments.add(OFFER_FRAGMENT)
            return OffersFragment()
        }
        if (!renderedFragments.contains(SURVEY_FRAGMENT) && availableFragments!!.contains(
                SURVEY_FRAGMENT
            )
        ) {
            renderedFragments.add(SURVEY_FRAGMENT)
            return SurveysFragment()
        }

        if (!renderedFragments.contains(AD_FRAGMENT) && availableFragments!!.contains(
                AD_FRAGMENT
            )
        ) {
            renderedFragments.add(AD_FRAGMENT)
            return AdsFragment()
        }

        return Fragment()
    }

    override fun getItemCount(): Int {
        return this.availableFragments!!.size
    }
}
