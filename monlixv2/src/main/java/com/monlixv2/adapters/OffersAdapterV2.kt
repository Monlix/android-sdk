package com.monlixv2.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.ui.activities.OfferDetailsActivity
import com.monlixv2.ui.activities.SearchOffersActivity
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.ui.fragments.SORT_FILTER
import com.monlixv2.ui.fragments.SORT_FILTER_TO_ID
import com.monlixv2.ui.fragments.SORT_IDS_TO_SORT_FILTER
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.ANDROID_CAMPAIGN_PARAM
import com.monlixv2.util.Constants.campaignCrComparator
import com.monlixv2.util.Constants.campaignHighToLowPayoutComparator
import com.monlixv2.util.Constants.campaignLowToHighPayoutComparator
import com.monlixv2.util.Constants.dateFormatter
import com.monlixv2.util.UIHelpers.dangerouslySetHTML



val SPINNER_POSITION_MAPPING = mapOf(Constants.ALL_OFFERS to 0, Constants.ANDROID to 1)

class OffersAdapterV2(
    private val dataSource: ArrayList<Campaign>,
    private val activity: AppCompatActivity,
) : RecyclerView.Adapter<OffersAdapterV2.OfferHolderV2>() {

    private var sortFilter: SORT_FILTER = SORT_FILTER.NONE
    private var typeOfOffersFilter = Constants.ALL_OFFERS
    private var filteredCampaigns = ArrayList<Campaign>()
    private var isFakeSelected = false

    init {
        filteredCampaigns = dataSource
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferHolderV2 {
        return OfferHolderV2(
            parent.inflate(
                when (viewType) {
                    FILTER_CARD -> R.layout.offer_filters
                    SIMPLE_OFFER_CARD -> R.layout.offer_item
                    else -> R.layout.offer_item_with_steps
                }, false
            )
        )

    }

    override fun getItemCount(): Int {
        return filteredCampaigns.size+1
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0)
            return  FILTER_CARD

        return if (filteredCampaigns[position].hasGoals && filteredCampaigns[position].goals.size > 0) STEP_OFFER_CARD else SIMPLE_OFFER_CARD
    }


    fun initListeners(holder: OfferHolderV2) {
        holder.sortFilters?.setOnClickListener {
            showOrderFilters()
        }
        holder.searchContainer?.setOnClickListener {
            val intent = Intent(activity, SearchOffersActivity::class.java)
            intent.putExtra(Constants.CAMPAIGNS_PAYLOAD,filteredCampaigns)
            activity.startActivity(intent);
            activity.overridePendingTransition( R.anim.slide_in_up, android.R.anim.fade_out);
        }
        holder.offerTypeSpinner?.adapter = ArrayAdapter<String>(
            activity, R.layout.simple_spinner_dropdown_item,
            Constants.OFFER_FILTER_LIST
        )
        isFakeSelected = true
        holder.offerTypeSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if(isFakeSelected) {
                    isFakeSelected = false
                    return
                }
                typeOfOffersFilter = adapterView.getItemAtPosition(i).toString()
                filterData()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        SPINNER_POSITION_MAPPING[typeOfOffersFilter]?.let { holder.offerTypeSpinner?.setSelection(it) }
    }

    private fun showOrderFilters() {
        val bottomSheetDialog = BottomSheetDialog(activity)
        bottomSheetDialog.setContentView(R.layout.sort_offers_bottom_sheet)

        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.sortGroup)
        if (sortFilter !== SORT_FILTER.NONE) {
            radioGroup?.check(SORT_FILTER_TO_ID[sortFilter]!!)
        }
        radioGroup?.setOnCheckedChangeListener { _, clickedId ->
            sortFilter = SORT_IDS_TO_SORT_FILTER[clickedId]!!
            filterData()
        }

        bottomSheetDialog.findViewById<ImageView>(R.id.closeSort)?.setOnClickListener {
            bottomSheetDialog.cancel()
        }
        bottomSheetDialog.show()
    }

    private fun filterData() {
        println("*** FILTERING ***")
         // filter by offer type
        filteredCampaigns = when (typeOfOffersFilter) {
            Constants.ALL_OFFERS -> filteredCampaigns
            else -> filteredCampaigns.filter { it ->
                Constants.ANDROID_CAMPAIGN_PARAM in it.oss
            } as ArrayList<Campaign>
        }

        // filter by sort
        if (sortFilter !== SORT_FILTER.NONE) {
            when (sortFilter) {
                SORT_FILTER.HIGH_TO_LOW -> filteredCampaigns.sortWith(
                    campaignHighToLowPayoutComparator
                )
                SORT_FILTER.LOW_TO_HIGH -> filteredCampaigns.sortWith(
                    campaignLowToHighPayoutComparator
                )
                SORT_FILTER.RECOMMENDED -> filteredCampaigns.sortWith(campaignCrComparator)
                else -> filteredCampaigns.sortByDescending { campaign -> dateFormatter.parse(campaign.createdAt) }
            }
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: OfferHolderV2, position: Int) {
        println("Binding OFFER ${position}")
        if (holder.itemViewType == FILTER_CARD) {
            initListeners(holder)
            return
        }
        holder.title?.let { dangerouslySetHTML(filteredCampaigns[position].name, it) }
        holder.description?.let { dangerouslySetHTML(filteredCampaigns[position].description, it) }
        holder.points?.text = "+${filteredCampaigns[position].payout}"
        holder.currency?.text = filteredCampaigns[position].currency
        holder.campaign = filteredCampaigns[position]

        holder.offerImage?.let {
            Glide.with(holder.itemView.context).load(filteredCampaigns[position].image)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.offer_placeholder))
                .into(it)
        }

        if (holder.itemViewType == STEP_OFFER_CARD) {
            holder.offerStepsCount?.text = "0/${filteredCampaigns[position].goals.size}"
            holder.progressView?.setProgress(5.0)
        }
        holder.statusNewUsers?.visibility = if(filteredCampaigns[position].multipleTimes) View.GONE else View.VISIBLE
        holder.statusAndroid?.visibility = if(filteredCampaigns[position].oss.contains(ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        holder.statusMultiReward?.visibility = if(filteredCampaigns[position].hasGoals) View.VISIBLE else View.GONE
        holder.statusCompleteTask?.visibility = if(filteredCampaigns[position].hasGoals) View.GONE else View.VISIBLE

        holder.startOfferBtn?.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: OfferHolderV2, campaign: Campaign) {
        val intent = Intent(holder.itemView.context, OfferDetailsActivity::class.java)
        intent.putExtra(Constants.SINGLE_CAMPAIGN_PAYLOAD,campaign)
        activity.startActivity(intent);
        activity.overridePendingTransition( R.anim.slide_in_up, android.R.anim.fade_out);
    }


    class OfferHolderV2(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView? = v.findViewById(R.id.offerTitle)
        val description: TextView? = v.findViewById(R.id.offerDescription)
        val points: TextView? = v.findViewById(R.id.offerPoints)
        val currency: TextView? = v.findViewById(R.id.offerCurrency)
        val offerImage: ImageView? = v.findViewById(R.id.offerImage)
        val progressView: SquareProgressView? = v.findViewById(R.id.offerProgress)
        val offerStepsCount: TextView? = v.findViewById(R.id.offerStepsCount)
        val startOfferBtn: TextView? = v.findViewById(R.id.startOfferBtn)
        val statusAndroid: TextView? = v.findViewById(R.id.status_item_android)
        val statusMultiReward: TextView? = v.findViewById(R.id.status_item_multireward)
        val statusCompleteTask: TextView? = v.findViewById(R.id.status_item_complete_task)
        val statusNewUsers: TextView? = v.findViewById(R.id.status_item_new_users)
        val sortFilters: LinearLayout? = v.findViewById(R.id.sortFilters)
        val searchContainer: CardView? = v.findViewById(R.id.searchContainer)
        val offerTypeSpinner: AppCompatSpinner? = v.findViewById(R.id.offerTypeSpinner)

        lateinit var campaign: Campaign

        init {
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }
    }
}

