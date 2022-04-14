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
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.ANDROID_CAMPAIGN_PARAM
import com.monlixv2.util.UIHelpers.dangerouslySetHTML


const val FILTER_CARD = 2
const val SIMPLE_OFFER_CARD = 0
const val STEP_OFFER_CARD = 1
const val TAG_EXPANDED = '1'
const val TAG_NOT_EXPANDED = '0'

class OffersAdapter(
    private val dataSource: ArrayList<Campaign>,
    private val activity: AppCompatActivity,
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
        println("Binding OFFER ${position}")
        dangerouslySetHTML(dataSource[position].name, holder.title)
        dangerouslySetHTML(dataSource[position].description, holder.description)
        holder.points.text = "+${dataSource[position].payout}"
        holder.currency.text = dataSource[position].currency
        holder.campaign = dataSource[position]

        Glide.with(holder.itemView.context).load(dataSource[position].image)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(holder.offerImage)

        if (holder.itemViewType == STEP_OFFER_CARD) {
            holder.offerStepsCount?.text = "0/${dataSource[position].goals.size}"
            holder.progressView?.setProgress(5.0)
        }
        holder.statusNewUsers.visibility = if(dataSource[position].multipleTimes) View.GONE else View.VISIBLE
        holder.statusAndroid.visibility = if(dataSource[position].oss.contains(ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        holder.statusMultiReward.visibility = if(dataSource[position].hasGoals) View.VISIBLE else View.GONE
        holder.statusCompleteTask.visibility = if(dataSource[position].hasGoals) View.GONE else View.VISIBLE

        holder.startOfferBtn.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: OfferHolder, campaign: Campaign) {
        val intent = Intent(holder.itemView.context, OfferDetailsActivity::class.java)
        intent.putExtra(Constants.SINGLE_CAMPAIGN_PAYLOAD,campaign)
        activity.startActivity(intent);
        activity.overridePendingTransition( R.anim.slide_in_up, android.R.anim.fade_out);
    }


    class OfferHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.offerTitle)
        val description: TextView = v.findViewById(R.id.offerDescription)
        val points: TextView = v.findViewById(R.id.offerPoints)
        val currency: TextView = v.findViewById(R.id.offerCurrency)
        val offerImage: ImageView = v.findViewById(R.id.offerImage)
        val progressView: SquareProgressView? = v.findViewById(R.id.offerProgress)
        val offerStepsCount: TextView? = v.findViewById(R.id.offerStepsCount)
        val startOfferBtn: TextView = v.findViewById(R.id.startOfferBtn)
        val statusAndroid: TextView = v.findViewById(R.id.status_item_android)
        val statusMultiReward: TextView = v.findViewById(R.id.status_item_multireward)
        val statusCompleteTask: TextView = v.findViewById(R.id.status_item_complete_task)
        val statusNewUsers: TextView = v.findViewById(R.id.status_item_new_users)

        lateinit var campaign: Campaign

        init {
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }
    }
}

