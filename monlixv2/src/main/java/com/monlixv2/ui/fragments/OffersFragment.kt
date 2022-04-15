package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapterV2
import com.monlixv2.adapters.SurveysAdapter
import com.monlixv2.databinding.OffersFragmentV2Binding
import com.monlixv2.viewmodels.OfferViewModel
//import com.monlixv2.viewmodels.OfferViewModelFactory
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants
import com.monlixv2.viewmodels.CampaignsViewModel
import com.monlixv2.viewmodels.SurveysViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class OffersFragment : Fragment(), CoroutineScope {
    private lateinit var binding: OffersFragmentV2Binding
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private lateinit var offersAdapterV2: OffersAdapterV2

    private val campaignsViewModel: CampaignsViewModel by viewModels {
        Constants.viewModelFactory {
            CampaignsViewModel((context?.applicationContext as App).campaignsRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.offers_fragment_v2, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    fun filterData(platform: Int, sortType: Constants.SORT_FILTER) {
        campaignsViewModel.getSortedCampaigns(platform, sortType).observe(this) {
            println("Aaaaaaaaaaaaaaaa ${it.size}")
            if (offersAdapterV2 != null) {
                offersAdapterV2.updateData(it)
            }
        }
    }


    private fun setupAdapter() {
        campaignsViewModel.allCampaigns.observe(viewLifecycleOwner) {
            offersAdapterV2 = OffersAdapterV2(it, context as AppCompatActivity, this@OffersFragment)
            binding.offersRecyclerV2.adapter = offersAdapterV2
        }
    }

}

