package com.monlixv2.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.OffersSearchAdapter
import com.monlixv2.databinding.OfferSearchActivityBinding
import com.monlixv2.util.Constants
import com.monlixv2.viewmodels.CampaignsViewModel


class SearchOffersActivity : AppCompatActivity() {
    private lateinit var binding: OfferSearchActivityBinding
    private  var offersAdapter: OffersSearchAdapter? = null

    private val campaignsViewModel: CampaignsViewModel by viewModels {
        Constants.viewModelFactory {
            CampaignsViewModel((applicationContext as App).campaignsRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.offer_search_activity)
        binding.lifecycleOwner = this

        offersAdapter = OffersSearchAdapter(this)
        binding.offerSearchRecycler.adapter = offersAdapter
        filterData()
    }

    fun filterData(title: String = "") {
        println(" FILTERINGGGG ")
        campaignsViewModel.searchCampaigns(title).observe(this) {
            println("Searched response ${it.size}")
            offersAdapter?.updateData(it)
        }
//        campaignsViewModel.featuredCampaigns.observe(this) {
//            println("featured " + it.size)
//        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_down)
    }


}
