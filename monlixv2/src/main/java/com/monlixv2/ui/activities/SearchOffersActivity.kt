package com.monlixv2.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.monlixv2.MonlixOffers
import com.monlixv2.R
import com.monlixv2.adapters.OffersSearchAdapter
import com.monlixv2.databinding.MonlixOfferSearchActivityBinding
import com.monlixv2.util.Constants
import com.monlixv2.util.RecyclerScrollListener
import com.monlixv2.viewmodels.CampaignsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SearchOffersActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: MonlixOfferSearchActivityBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private var offersAdapter: OffersSearchAdapter? = null
    private var isLoadingFromDb = false
    private var isDbLastPage = false
    private var isScrollListenerAttached = false

    private val campaignsViewModel: CampaignsViewModel by viewModels {
        Constants.viewModelFactory {
            CampaignsViewModel(MonlixOffers.campaignsRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.monlix_offer_search_activity)
        binding.lifecycleOwner = this

        setupAdapter()
    }

    private fun setupAdapter() {
        offersAdapter = OffersSearchAdapter(this)
        binding.offerSearchRecycler.adapter = offersAdapter
    }

    fun filterData(title: String, offset: Int) {
        lifecycleScope.launch {
            campaignsViewModel.searchCampaignsByName(title, offset).collect {
                if (it.isEmpty() && offersAdapter?.currentOffset != 0) {
                    isDbLastPage = true
                    return@collect
                }

                if (offersAdapter == null){
                    return@collect
                }

                isLoadingFromDb = false
                isDbLastPage = false

                if (offersAdapter?.currentOffset != 0) {
                    offersAdapter?.appendData(it)
                } else {
                    offersAdapter?.replaceData(it)
                }

                if (!isScrollListenerAttached) {
                    attachScrollListener()
                    lookForFeatured()
                }
            }
        }
    }
    private fun lookForFeatured(){
        lifecycleScope.launch {
            campaignsViewModel.featuredCampaigns.collect {
                if(offersAdapter != null) {
                    offersAdapter?.setFeatured(it)
                }
            }
        }
    }

    private fun attachScrollListener() {
        isScrollListenerAttached = true
        binding.offerSearchRecycler.addOnScrollListener(object :
            RecyclerScrollListener(binding.offerSearchRecycler.layoutManager!! as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoadingFromDb = true
                offersAdapter?.loadNextPage()
            }

            override val isLastPage: Boolean
                get() = isDbLastPage
            override val isLoading: Boolean
                get() = isLoadingFromDb
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.monlix_slide_out_down)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
