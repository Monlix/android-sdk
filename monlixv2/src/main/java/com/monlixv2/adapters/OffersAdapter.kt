package com.monlixv2.adapters

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monlixv2.R
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants

const val SIMPLE_OFFER_CARD = 0
const val STEP_OFFER_CARD = 1

class OffersAdapter(
    private val dataSource: ArrayList<Campaign>
) : RecyclerView.Adapter<OffersAdapter.OfferHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferHolder {
        return OfferHolder(
            parent.inflate(
                when (viewType) {
                    SIMPLE_OFFER_CARD -> R.layout.offer_item
                    else -> R.layout.offer_item_with_steps
                }, false
            )
        )

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSource[position].hasGoals && dataSource[position].goals.size > 0) STEP_OFFER_CARD else SIMPLE_OFFER_CARD
    }


    override fun onBindViewHolder(holder: OfferHolder, position: Int) {
        holder.title.text = dataSource[position].name
        holder.points.text = "${dataSource[position].payout} \n ${dataSource[position].currency}"
        holder.description.text = dataSource[position].description
        holder.campaign = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].image)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.offerImage)

        if (holder.itemViewType == STEP_OFFER_CARD) {
            holder.offerStepsCount?.text = "0/${dataSource[position].goals.size}"
            holder.progressView?.setProgress(5.0)
        }


    }


    class OfferHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val title: TextView = v.findViewById(R.id.offerTitle)
        val description: TextView = v.findViewById(R.id.offerDescription)
        val points: TextView = v.findViewById(R.id.offerPoints)
        val offerImage: ImageView = v.findViewById(R.id.offerImage)
        val progressView: SquareProgressView? = v.findViewById(R.id.offerProgress)
        val offerStepsCount: TextView? = v.findViewById(R.id.offerStepsCount)
        lateinit var campaign: Campaign

        init {
            v.setOnClickListener(this)
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }

        override fun onClick(v: View?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(campaign.url)
            v?.let { ContextCompat.startActivity(it.context, i, null) }
        }
    }
}

