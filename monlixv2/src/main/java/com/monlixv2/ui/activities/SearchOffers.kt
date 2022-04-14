package com.monlixv2.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapter
import com.monlixv2.databinding.OfferSearchActivityBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants.CAMPAIGNS_PAYLOAD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SearchOffers : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: OfferSearchActivityBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private var textFilter = ""
    private var allCampaigns: ArrayList<Campaign> = ArrayList()
    private var filteredCampaigns: ArrayList<Campaign> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.offer_search_activity)
        binding.lifecycleOwner = this

        allCampaigns =
            intent.getParcelableArrayListExtra<Campaign>(CAMPAIGNS_PAYLOAD) as ArrayList<Campaign>

        binding.textSearch.addTextChangedListener(textWatcher)
        displayData()
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
                displayData()
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }


    fun displayData() {
        filteredCampaigns = if (textFilter.isNotEmpty()) {
            allCampaigns.filter { it ->
                it.name.lowercase().contains(textFilter)
            } as ArrayList<Campaign>
        } else {
            allCampaigns
        }
        binding.searchRecycler.apply {
            adapter = OffersAdapter(filteredCampaigns, this@SearchOffers)
        }
    }

    fun close(view: View) {
        finish();
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_down)
    }

}
