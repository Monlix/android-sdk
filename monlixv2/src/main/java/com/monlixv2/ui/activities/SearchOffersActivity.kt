package com.monlixv2.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.monlixv2.R
import com.monlixv2.adapters.OffersSearchAdapter
import com.monlixv2.databinding.OfferSearchActivityBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants.CAMPAIGNS_PAYLOAD


class SearchOffersActivity : AppCompatActivity() {
    private lateinit var binding: OfferSearchActivityBinding
    private var allCampaigns: ArrayList<Campaign> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.offer_search_activity)
        binding.lifecycleOwner = this

        allCampaigns =
            intent.getParcelableArrayListExtra<Campaign>(CAMPAIGNS_PAYLOAD) as ArrayList<Campaign>

        val featured = allCampaigns.filter { campaign -> campaign.featured }
        binding.offerSearchRecycler.apply {
            adapter = OffersSearchAdapter(allCampaigns, featured as ArrayList<Campaign>,this@SearchOffersActivity)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_down)
    }

}
