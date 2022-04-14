package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.monlixv2.R
import com.monlixv2.adapters.PagerAdapter
import com.monlixv2.databinding.HomeFragmentBinding
import com.monlixv2.ui.activities.MainActivity
import com.monlixv2.viewmodels.GroupedResponse

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private var tabs = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.monlixPager.offscreenPageLimit = 1
    }

    private fun setupTabNames (apiResponse: GroupedResponse){
        tabs = arrayListOf()
        if(apiResponse.mergedSurveys!!.size > 0) {
            tabs.add("Surveys")
        }
        if(apiResponse.campaigns!!.size > 0) {
            tabs.add("Offers")
        }
        if(apiResponse.offers!!.ads.size > 0) {
            tabs.add("Ads")
        }
    }

    fun displayData(apiResponse: GroupedResponse) {
        val adapter = PagerAdapter(requireActivity() as MainActivity);
        setupTabNames(apiResponse)
        adapter.setupData(apiResponse)
        binding.monlixPager.adapter = adapter
        TabLayoutMediator(binding.monlixTablayout, binding.monlixPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

}
