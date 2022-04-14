package com.monlixv2.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.monlixv2.R
import com.monlixv2.adapters.FeaturedOffersAdapter
import com.monlixv2.databinding.OfferSearchActivityBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants.CAMPAIGNS_PAYLOAD
import com.monlixv2.util.UIHelpers.dangerouslySetHTML
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SearchOffersActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: OfferSearchActivityBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private var textFilter = ""
    private var allCampaigns: ArrayList<Campaign> = ArrayList()
    private var filteredCampaigns: ArrayList<Campaign> = ArrayList()
    private var featuredCampaigns: ArrayList<Campaign> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.offer_search_activity)
        binding.lifecycleOwner = this

        allCampaigns =
            intent.getParcelableArrayListExtra<Campaign>(CAMPAIGNS_PAYLOAD) as ArrayList<Campaign>

        binding.textSearch.addTextChangedListener(textWatcher)
        displayFeatured()
        displayResults()
    }

    private val textWatcher = object : TextWatcher {
        private var searchFor = ""

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val searchText = s.toString().trim()
            if (searchText == searchFor)
                return

            searchFor = searchText

            launch {
                delay(300)  //debounce timeOut
                if (searchText != searchFor)
                    return@launch
                textFilter = searchFor.lowercase()
                displayResults()
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }

    private fun filterData() {
        println(allCampaigns[0])
        filteredCampaigns = if (textFilter.isNotEmpty()) {
            allCampaigns.filter { it ->
                it.name.lowercase().contains(textFilter)
            } as ArrayList<Campaign>
        } else {
            allCampaigns
        }
        if (filteredCampaigns.size > 0) {
            binding.discoverText.text = "Results for ``${textFilter}``"
            manageFeaturedVisibility(View.GONE)
        }
        if (filteredCampaigns.size == 0 && allCampaigns.size > 0) {
            binding.noResultsContainer.visibility = View.VISIBLE
            dangerouslySetHTML(
                "No results for <b>`${textFilter}`</b>",
                binding.noResultsText
            )
            binding.discoverText.text = "Discover offers"
            if (featuredCampaigns.size > 0) {
                manageFeaturedVisibility(View.VISIBLE)
            }
        } else {
            binding.noResultsContainer.visibility = View.GONE
        }
        if (textFilter.contentEquals("")) {
            binding.discoverText.text = "Discover offers"
            if(featuredCampaigns.size > 0 ) {
                manageFeaturedVisibility(View.VISIBLE)
            }
        }
        if (filteredCampaigns.size == 0 && textFilter.isNotEmpty()) {
            filteredCampaigns = allCampaigns
        }
    }

    fun manageFeaturedVisibility(visible: Int) {
        binding.featuredText.visibility = visible
        binding.featuredRecycler.visibility = visible
    }

    private fun displayFeatured() {
        featuredCampaigns = allCampaigns.filter { el -> el.featured } as ArrayList<Campaign>
        if (featuredCampaigns.isNotEmpty()) {
            manageFeaturedVisibility(View.VISIBLE)
            binding.featuredRecycler.apply {
                adapter = FeaturedOffersAdapter(featuredCampaigns, this@SearchOffersActivity)
            }
        }
    }

    private fun displayResults() {
        filterData()
    }

    fun close(view: View) {
        finish();
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_down)
    }

}
