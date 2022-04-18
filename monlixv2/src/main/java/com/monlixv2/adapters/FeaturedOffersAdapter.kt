package com.monlixv2.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.ui.activities.OfferDetailsActivity
import com.monlixv2.util.Constants
import com.monlixv2.util.UIHelpers.dangerouslySetHTML


class FeaturedOffersAdapter(
    private val dataSource: ArrayList<Campaign>,
    private val activity: AppCompatActivity,
) : RecyclerView.Adapter<FeaturedOffersAdapter.FeaturedOffersAdapter>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeaturedOffersAdapter {
        return FeaturedOffersAdapter(
            parent.inflate(
                R.layout.monlix_featured_item
            )
        )

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }


    override fun onBindViewHolder(holder: FeaturedOffersAdapter, position: Int) {
        dangerouslySetHTML(dataSource[position].name, holder.title)
        holder.points.text = "+${dataSource[position].payout}"
        holder.currency.text = dataSource[position].currency
        holder.campaign = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].image)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.monlix_offer_placeholder))
            .into(holder.offerImage)


        holder.itemView.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: FeaturedOffersAdapter, campaign: Campaign) {
        val intent = Intent(holder.itemView.context, OfferDetailsActivity::class.java)
        val campaignJson = Constants.campaignToJSON(campaign)
        intent.putExtra(Constants.SINGLE_CAMPAIGN_PAYLOAD, campaignJson)
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.monlix_slide_in_up, android.R.anim.fade_out);
    }


    class FeaturedOffersAdapter(v: View) : RecyclerView.ViewHolder(v) {
        val offerImage: ImageView = v.findViewById(R.id.offerImage)
        val title: TextView = v.findViewById(R.id.offerTitle)
        val points: TextView = v.findViewById(R.id.offerPoints)
        val currency: TextView = v.findViewById(R.id.offerCurrency)

        lateinit var campaign: Campaign
    }
}

