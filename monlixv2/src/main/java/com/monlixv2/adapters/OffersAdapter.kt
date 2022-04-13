package com.monlixv2.adapters

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.util.Constants.ANDROID_CAMPAIGN_PARAM


const val SIMPLE_OFFER_CARD = 0
const val STEP_OFFER_CARD = 1
const val TAG_EXPANDED = '1'
const val TAG_NOT_EXPANDED = '0'

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
        holder.points.text = "+${dataSource[position].payout}"
        holder.currency.text = dataSource[position].currency
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
        holder.statusNewUsers.visibility = if(dataSource[position].multipleTimes) View.GONE else View.VISIBLE
        holder.statusAndroid.visibility = if(dataSource[position].oss.contains(ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        holder.statusMultiReward.visibility = if(dataSource[position].hasGoals) View.VISIBLE else View.GONE
        holder.statusCompleteTask.visibility = if(dataSource[position].hasGoals) View.GONE else View.VISIBLE

        holder.startOfferBtn.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: OfferHolder, campaign: Campaign) {
        val bottomSheetDialog = BottomSheetDialog(holder.itemView.context)
        bottomSheetDialog.setContentView(R.layout.ad_bottom_sheet)

        bottomSheetDialog.findViewById<View>(R.id.spacer)!!.minimumHeight = (Resources.getSystem().displayMetrics.heightPixels) / 2
        bottomSheetDialog.findViewById<TextView>(R.id.title)?.text = "Requirements"
        bottomSheetDialog.findViewById<TextView>(R.id.adTitle)?.text = campaign.name
        bottomSheetDialog.findViewById<TextView>(R.id.transactionId)?.text = campaign.description
        bottomSheetDialog.findViewById<Button>(R.id.startOfferBtn)!!.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(campaign.url)
            it?.let { ContextCompat.startActivity(it.context, i, null) }
        }


        val logo = bottomSheetDialog.findViewById<ImageView>(R.id.transactionImage)

        Glide.with(holder.itemView.context).load(campaign.image)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
            .into(logo!!)

        if (campaign.hasGoals && campaign.goals.size > 0) {
            val stepsToggle = bottomSheetDialog.findViewById<TextView>(R.id.stepsToggle)!!
            val stepsContainer = bottomSheetDialog.findViewById<LinearLayout>(R.id.stepsContainer)!!;
            val stepsScroller = stepsContainer.parent as ScrollView
            stepsScroller.apply {
                setOnTouchListener(View.OnTouchListener { v, _ ->
                    v.parent.parent.parent.requestDisallowInterceptTouchEvent(true);
                    performClick()
                    false
                })
            }
            stepsToggle.visibility = View.VISIBLE
            stepsToggle.tag = TAG_NOT_EXPANDED

            for (i in 0 until campaign.goals.size) {
                val requirementItem = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.requrement_item, null, false);

                val textview =
                    requirementItem.findViewById<TextView>(R.id.requirementTitle)
                val stepCheck = requirementItem.findViewById<ImageView>(R.id.stepCheck)
                val goalPayout =
                    requirementItem.findViewById<TextView>(R.id.goalPayout);
                textview.text = campaign.goals[i].name
                goalPayout.text = campaign.goals[i].payout

                Glide.with(holder.itemView.context).load(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.circle_radio
                    )
                ).into(stepCheck)
                if (i == campaign.goals.size - 1) {
                    requirementItem.findViewById<ImageView>(R.id.bottomDots).visibility =
                        View.GONE
                }
                stepsContainer.addView(requirementItem)
            }

            stepsToggle.setOnClickListener {
                val shouldReveal = stepsToggle.tag == TAG_NOT_EXPANDED
                stepsContainer.visibility = if(shouldReveal) View.VISIBLE else View.GONE
                stepsScroller.visibility = if(shouldReveal) View.VISIBLE else View.GONE

                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    if (shouldReveal) R.drawable.arrow_down_reversed else
                        R.drawable.arrow_down
                )
                stepsToggle.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )

                stepsToggle.tag =
                    if (stepsToggle.tag == TAG_EXPANDED) TAG_NOT_EXPANDED else TAG_EXPANDED
            }
            stepsToggle.performClick()
        }

        bottomSheetDialog.findViewById<ImageView>(R.id.adSheetClose)?.setOnClickListener {
            bottomSheetDialog.cancel()
        }

        bottomSheetDialog.behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
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

