package com.monlixv2.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlixv2.R
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.campaigns.DEFAULT_LIMIT
import com.monlixv2.service.models.campaigns.PLATFORM_ALL
import com.monlixv2.service.models.campaigns.PLATFORM_ANDROID
import com.monlixv2.ui.activities.OfferDetailsActivity
import com.monlixv2.ui.activities.SearchOffersActivity
import com.monlixv2.ui.components.squareprogressbar.SquareProgressView
import com.monlixv2.ui.fragments.OffersFragment
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.ANDROID_CAMPAIGN_PARAM
import com.monlixv2.util.Constants.SORT_FILTER_TO_ID
import com.monlixv2.util.Constants.SORT_IDS_TO_SORT_FILTER
import com.monlixv2.util.Constants.campaignToJSON
import com.monlixv2.util.UIHelpers.dangerouslySetHTML


val SPINNER_POSITION_MAPPING = mapOf(Constants.ALL_OFFERS to 0, Constants.ANDROID to 1)
val FILTER_TYPE_TO_DB_TYPE =
    mapOf(Constants.ALL_OFFERS to PLATFORM_ALL, Constants.ANDROID to PLATFORM_ANDROID)

const val FILTER_CARD = 2
const val SIMPLE_OFFER_CARD = 0
const val STEP_OFFER_CARD = 1
const val TAG_EXPANDED = '1'
const val TAG_NOT_EXPANDED = '0'

class OffersAdapterV2(
    private val activity: AppCompatActivity,
    private val fragmentInstance: OffersFragment
) : RecyclerView.Adapter<OffersAdapterV2.OfferHolderV2>() {

    private var sortFilter: Constants.SORT_FILTER = Constants.SORT_FILTER.NONE
    private var typeOfOffersFilter = Constants.ALL_OFFERS
    private var filteredCampaigns: ArrayList<Campaign> = ArrayList<Campaign>()
    private var spinnerAutoSelected = false
    private var isInitialLoad = true
    var currentOffset = 0

    init {
//        filteredCampaigns = dataSource
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
        return filteredCampaigns.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return FILTER_CARD

        return if (filteredCampaigns[position - 1].hasGoals && filteredCampaigns[position - 1].goals.size > 0) STEP_OFFER_CARD else SIMPLE_OFFER_CARD
    }


    fun initListeners(holder: OfferHolderV2) {
        holder.sortFilters?.setOnClickListener {
            showOrderFilters()
        }
        holder.searchContainer?.setOnClickListener {
            val intent = Intent(activity, SearchOffersActivity::class.java)
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_up, android.R.anim.fade_out);
        }
        holder.offerTypeSpinner?.adapter = ArrayAdapter<String>(
            activity, R.layout.simple_spinner_dropdown_item,
            Constants.OFFER_FILTER_LIST
        )
        spinnerAutoSelected = if (isInitialLoad) false else true
        holder.offerTypeSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (spinnerAutoSelected) {
                    spinnerAutoSelected = false
                    return
                }
                isInitialLoad = false
                typeOfOffersFilter = adapterView.getItemAtPosition(i).toString()
                currentOffset = 0
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
        if (sortFilter !== Constants.SORT_FILTER.NONE) {
            radioGroup?.check(SORT_FILTER_TO_ID[sortFilter]!!)
        }
        radioGroup?.setOnCheckedChangeListener { _, clickedId ->
            sortFilter = SORT_IDS_TO_SORT_FILTER[clickedId]!!
            currentOffset = 0
            filterData()
        }

        bottomSheetDialog.findViewById<ImageView>(R.id.closeSort)?.setOnClickListener {
            bottomSheetDialog.cancel()
        }
        bottomSheetDialog.show()
    }

    fun filterData() {
        fragmentInstance.filterData(
            FILTER_TYPE_TO_DB_TYPE[typeOfOffersFilter]!!,
            sortFilter,
            currentOffset
        )
    }

    fun loadNextPage() {
        currentOffset += DEFAULT_LIMIT
        filterData()
    }


    override fun onBindViewHolder(holder: OfferHolderV2, position: Int) {

        if (holder.itemViewType == FILTER_CARD) {
            initListeners(holder)
            return
        }

        holder.title?.let { dangerouslySetHTML(filteredCampaigns[position - 1].name, it) }
        holder.description?.let {
            dangerouslySetHTML(
                filteredCampaigns[position - 1].description,
                it
            )
        }
        holder.points?.text = "+${filteredCampaigns[position - 1].payout}"
        holder.currency?.text = filteredCampaigns[position - 1].currency
        holder.campaign = filteredCampaigns[position - 1]

        holder.offerImage?.let {
            Glide.with(holder.itemView.context).load(filteredCampaigns[position - 1].image)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.offer_placeholder
                    )
                )
                .into(it)
        }

        if (holder.itemViewType == STEP_OFFER_CARD) {
            holder.offerStepsCount?.text = "0/${filteredCampaigns[position - 1].goals.size}"
            holder.progressView?.setProgress(5.0)
        }
        holder.statusNewUsers?.visibility =
            if (filteredCampaigns[position - 1].multipleTimes) View.GONE else View.VISIBLE
        holder.statusAndroid?.visibility =
            if (filteredCampaigns[position - 1].oss.contains(ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        holder.statusMultiReward?.visibility =
            if (filteredCampaigns[position - 1].hasGoals) View.VISIBLE else View.GONE
        holder.statusCompleteTask?.visibility =
            if (filteredCampaigns[position - 1].hasGoals) View.GONE else View.VISIBLE

        holder.startOfferBtn?.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: OfferHolderV2, campaign: Campaign) {
        val intent = Intent(holder.itemView.context, OfferDetailsActivity::class.java)
        val campaignJson = campaignToJSON(campaign)
        intent.putExtra(Constants.SINGLE_CAMPAIGN_PAYLOAD, campaignJson)
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, android.R.anim.fade_out);
    }


    fun replaceData(it: List<Campaign>) {
        if (it != null) {
            filteredCampaigns = it as ArrayList<Campaign>
            notifyDataSetChanged()
        }
    }

    fun appendData(it: List<Campaign>?) {
        val currentPosition = filteredCampaigns.size
        it?.let { it1 -> filteredCampaigns.addAll(it1) }
        notifyItemRangeInserted(currentPosition+1, it!!.size)
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

