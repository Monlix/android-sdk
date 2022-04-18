package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.monlixv2.R
import com.monlixv2.adapters.AD_FRAGMENT
import com.monlixv2.adapters.OFFER_FRAGMENT
import com.monlixv2.adapters.PagerAdapter
import com.monlixv2.adapters.SURVEY_FRAGMENT
import com.monlixv2.databinding.MonlixHomeFragmentBinding
import com.monlixv2.ui.activities.MainActivity
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.FRAGMENT_NAME_ADS
import com.monlixv2.util.Constants.FRAGMENT_NAME_OFFERS
import com.monlixv2.util.Constants.FRAGMENT_NAME_SURVEYS


class HomeFragment : Fragment() {

    private lateinit var binding: MonlixHomeFragmentBinding
    private var tabs = arrayListOf<String>()
    private var availableFragments: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        availableFragments = arguments?.getIntArray(Constants.AVAILABLE_FRAGMENTS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.monlix_home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.monlixPager.offscreenPageLimit = 1
        if (availableFragments != null) {
            setupPager()
        }
    }

    private fun setupTabNames(availableFragments: IntArray) {
        tabs = arrayListOf()
        if (availableFragments.contains(OFFER_FRAGMENT)) {
            tabs.add(FRAGMENT_NAME_OFFERS)
        }
        if (availableFragments.contains(SURVEY_FRAGMENT)) {
            tabs.add(FRAGMENT_NAME_SURVEYS)
        }
        if (availableFragments.contains(AD_FRAGMENT)) {
            tabs.add(FRAGMENT_NAME_ADS)
        }
    }

    private fun setupPager() {
        val adapter = PagerAdapter(requireActivity() as MainActivity);
        setupTabNames(availableFragments!!)
        adapter.setupData(availableFragments!!)

        binding.monlixPager.adapter = adapter
        TabLayoutMediator(binding.monlixTablayout, binding.monlixPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

}
