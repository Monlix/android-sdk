package com.monlixv2.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.R
import com.monlixv2.adapters.TAG_EXPANDED
import com.monlixv2.adapters.TAG_NOT_EXPANDED
import com.monlixv2.databinding.OfferDetailsActivityBinding
import com.monlixv2.databinding.OfferItemInPopWindowBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.JSONToCampaign
import com.monlixv2.util.Constants.SINGLE_CAMPAIGN_PAYLOAD
import com.monlixv2.util.UIHelpers


class OfferDetailsActivity : AppCompatActivity() {

    private lateinit var binding: OfferDetailsActivityBinding
    private lateinit var offerDetailsBinding: OfferItemInPopWindowBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.offer_details_activity)
        offerDetailsBinding = binding.offerDetails
        binding.lifecycleOwner = this

        val campaignJSON = intent.getStringExtra(SINGLE_CAMPAIGN_PAYLOAD)
        val campaign: Campaign = JSONToCampaign(campaignJSON!!)

        displayData(campaign)
    }

    private fun displayData(campaign: Campaign) {


        binding.newUsersLayout.visibility = if(campaign.multipleTimes) View.GONE else View.VISIBLE
        binding.androidLayout.visibility = if(campaign.oss.contains(Constants.ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        binding.multiRewardLayout.visibility = if(campaign.hasGoals) View.VISIBLE else View.GONE
        binding.completeTasksLayout.visibility = if(campaign.hasGoals) View.GONE else View.VISIBLE

        UIHelpers.dangerouslySetHTML(campaign.name,binding.adTitle)
        UIHelpers.dangerouslySetHTML(campaign.description,offerDetailsBinding.description)

        binding.startOfferBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(campaign.url)
            it?.let { ContextCompat.startActivity(it.context, i, null) }
        }


        Glide.with(this).load(campaign.image)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(this, R.drawable.offer_placeholder))
            .into(offerDetailsBinding.transactionImage)

        if (campaign.hasGoals && campaign.goals.isNotEmpty()) {
            offerDetailsBinding.stepsScroller.apply {
                setOnTouchListener { v, _ ->
                    v.parent.parent.parent.requestDisallowInterceptTouchEvent(true);
                    performClick()
                    false
                }
            }
            offerDetailsBinding.stepsToggle.visibility = View.VISIBLE
            offerDetailsBinding.stepsToggle.tag = TAG_NOT_EXPANDED

            for (i in 0 until campaign.goals.size) {
                val requirementItem = LayoutInflater.from(this)
                    .inflate(R.layout.requrement_item, null, false);

                val textview =
                    requirementItem.findViewById<TextView>(R.id.requirementTitle)
                val stepCheck = requirementItem.findViewById<ImageView>(R.id.stepCheck)
                val goalPayout =
                    requirementItem.findViewById<TextView>(R.id.goalPayout);
                textview.text = campaign.goals[i].name
                goalPayout.text = campaign.goals[i].payout

                Glide.with(this).load(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.circle_radio
                    )
                ).into(stepCheck)
                if (i == campaign.goals.size - 1) {
                    requirementItem.findViewById<ImageView>(R.id.bottomDots).visibility =
                        View.GONE
                }
                offerDetailsBinding.stepsContainer.addView(requirementItem)
            }

            offerDetailsBinding.stepsToggle.setOnClickListener {
                val shouldReveal = offerDetailsBinding.stepsToggle.tag == TAG_NOT_EXPANDED
                offerDetailsBinding.stepsContainer.visibility =
                    if (shouldReveal) View.VISIBLE else View.GONE
                offerDetailsBinding.stepsScroller.visibility =
                    if (shouldReveal) View.VISIBLE else View.GONE

                val drawable = ContextCompat.getDrawable(
                    this,
                    if (shouldReveal) R.drawable.arrow_down_reversed else
                        R.drawable.arrow_down
                )
                offerDetailsBinding.stepsToggle.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )

                offerDetailsBinding.stepsToggle.tag =
                    if (offerDetailsBinding.stepsToggle.tag == TAG_EXPANDED) TAG_NOT_EXPANDED else TAG_EXPANDED
            }
            offerDetailsBinding.stepsToggle.performClick()
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
