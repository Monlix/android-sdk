package com.monlixv2.adapters

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

const val FILTER_SECTION = 2
const val DISCOVER_OFFERS_STR = "Discover offers"
class OffersSearchAdapter(
    private val dataSource: ArrayList<Campaign>,
    private val featuredCampaigns: ArrayList<Campaign>,
    private val activity: AppCompatActivity,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CoroutineScope {

    private var filteredCampaigns = ArrayList<Campaign>()
    private var textFilter = ""
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private var discoverString = DISCOVER_OFFERS_STR
    private var noResultsString = ""
    private var showFeatured = false

    init {
        filteredCampaigns = dataSource
        showFeatured = featuredCampaigns.size > 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        if (viewType == FILTER_SECTION) {
            return FilterHolder(
                parent.inflate(
                    R.layout.offer_search_filters,
                    false
                )
            )
        }
        return OfferHolder(parent.inflate(R.layout.offer_item, false))

    }

    override fun getItemCount(): Int {
        return filteredCampaigns.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return FILTER_SECTION

        return if (filteredCampaigns[position-1].hasGoals && filteredCampaigns[position-1].goals.size > 0) STEP_OFFER_CARD else SIMPLE_OFFER_CARD
    }


    fun initListeners(holder: FilterHolder) {
        dangerouslySetHTML(discoverString, holder.discoverText)
        dangerouslySetHTML(noResultsString, holder.noResultsText)
        holder.noResultsContainer.visibility = if(noResultsString.isEmpty()) View.GONE else View.VISIBLE
        holder.featuredText.visibility = if(showFeatured) View.VISIBLE else View.GONE
        holder.featuredRecycler.visibility = if(showFeatured) View.VISIBLE else View.GONE

        if(showFeatured) {
            holder.featuredRecycler.apply {
                adapter = FeaturedOffersAdapter(featuredCampaigns, activity)
            }
        }
//
        //noResultsContainer
        holder.textSearch.addTextChangedListener(textWatcher)
        holder.closeImg.setOnClickListener {
            activity.finish()
        }
    }

    private val textWatcher = object : TextWatcher {
        private var searchFor = ""

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val searchText = s.toString().trim()
            if (searchText == searchFor)
                return

            searchFor = searchText

            textFilter = searchFor.lowercase()

            launch {
                delay(300)  //debounce timeOut
                if (searchText != searchFor)
                    return@launch
                textFilter = searchFor.lowercase()
                filterData()
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }


    fun filterData() {
        println("*** FILTERING *** ${textFilter}. Size before ${filteredCampaigns.size}")
        // filter by offer type
        filteredCampaigns = if (textFilter.isNotEmpty()) {
            dataSource.filter { it ->
                it.name.lowercase().contains(textFilter)
            } as ArrayList<Campaign>
        } else {
            dataSource
        }


        if (filteredCampaigns.size > 0) {
            discoverString = "Results for ``${textFilter}``"
            noResultsString = ""
            showFeatured = false
        }
        if (filteredCampaigns.size == 0 && dataSource.size > 0) {
            noResultsString = "No results for <b>`${textFilter}`</b>"
            discoverString = DISCOVER_OFFERS_STR
            if(featuredCampaigns.size > 0 ) {
                showFeatured = true
            }
        } else {
            noResultsString = ""
        }
        if (textFilter.contentEquals("")) {
            discoverString = DISCOVER_OFFERS_STR
            if(featuredCampaigns.size > 0 ) {
                showFeatured = true
            }
        }
        if (filteredCampaigns.size == 0 && textFilter.isNotEmpty()) {
            filteredCampaigns = dataSource
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println("Binding OFFER ${position}")

        if (holder.itemViewType == FILTER_CARD) {
            initListeners(holder as FilterHolder)
            return
        }
        (holder as OfferHolder).title?.let {
            dangerouslySetHTML(
                filteredCampaigns[position-1].name,
                it
            )
        }
        holder.description?.let {
            dangerouslySetHTML(
                filteredCampaigns[position-1].description,
                it
            )
        }
        holder.points?.text = "+${filteredCampaigns[position-1].payout}"
        holder.currency?.text = filteredCampaigns[position-1].currency
        holder.campaign = filteredCampaigns[position-1]

        holder.offerImage?.let {
            Glide.with(holder.itemView.context).load(filteredCampaigns[position-1].image)
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
            holder.offerStepsCount?.text = "0/${filteredCampaigns[position-1].goals.size}"
            holder.progressView?.setProgress(5.0)
        }
        holder.statusNewUsers?.visibility = if(filteredCampaigns[position-1].multipleTimes) View.GONE else View.VISIBLE
        holder.statusAndroid?.visibility = if(filteredCampaigns[position-1].oss.contains(ANDROID_CAMPAIGN_PARAM)) View.VISIBLE else View.GONE
        holder.statusMultiReward?.visibility = if(filteredCampaigns[position-1].hasGoals) View.VISIBLE else View.GONE
        holder.statusCompleteTask?.visibility = if(filteredCampaigns[position-1].hasGoals) View.GONE else View.VISIBLE

        holder.startOfferBtn?.setOnClickListener {
            onOfferClick(holder, holder.campaign)
        }

    }

    private fun onOfferClick(holder: OfferHolder, campaign: Campaign) {
        val intent = Intent(holder.itemView.context, OfferDetailsActivity::class.java)
//        intent.putExtra(Constants.SINGLE_CAMPAIGN_PAYLOAD, campaign)
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, android.R.anim.fade_out);
    }

    class FilterHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textSearch: EditText = v.findViewById(R.id.textSearch)
        val noResultsText: TextView = v.findViewById(R.id.noResultsText)
        val noResultsContainer: LinearLayout = v.findViewById(R.id.noResultsContainer)
        val featuredText: TextView = v.findViewById(R.id.featuredText)
        val discoverText: TextView = v.findViewById(R.id.discoverText)
        val closeImg: ImageView = v.findViewById(R.id.closeSearch);
        val featuredRecycler: RecyclerView = v.findViewById(R.id.featuredRecycler);
    }

    class OfferHolder(v: View) : RecyclerView.ViewHolder(v) {
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

        lateinit var campaign: Campaign

        init {
            progressView?.setWidthInDp(3)
            progressView?.setColor(ContextCompat.getColor(v.context, R.color.orangeV1))
            progressView?.setRoundedCorners(true, 8f)
        }
    }

}

