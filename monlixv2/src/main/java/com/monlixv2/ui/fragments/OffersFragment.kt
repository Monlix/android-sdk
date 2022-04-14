package com.monlixv2.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapter
import com.monlixv2.adapters.OffersAdapterV2
import com.monlixv2.databinding.OffersFragmentBinding
import com.monlixv2.databinding.OffersFragmentV2Binding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.ui.activities.SearchOffersActivity
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.ALL_OFFERS
import com.monlixv2.util.Constants.CAMPAIGNS_PAYLOAD
import com.monlixv2.util.Constants.campaignCrComparator
import com.monlixv2.util.Constants.campaignHighToLowPayoutComparator
import com.monlixv2.util.Constants.campaignLowToHighPayoutComparator
import com.monlixv2.util.Constants.dateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


private const val OFFERS_PARAM = "OFFERS_PARAM"

enum class SORT_FILTER {
    RECOMMENDED,
    HIGH_TO_LOW,
    LOW_TO_HIGH,
    NEWEST,
    NONE
}

val SORT_IDS_TO_SORT_FILTER = arrayMapOf(
    R.id.sortRecommended to SORT_FILTER.RECOMMENDED,
    R.id.sortHighToLow to SORT_FILTER.HIGH_TO_LOW,
    R.id.sortLowToHigh to SORT_FILTER.LOW_TO_HIGH,
    R.id.sortNewest to SORT_FILTER.NEWEST,
)

val SORT_FILTER_TO_ID = arrayMapOf(
    SORT_FILTER.RECOMMENDED to R.id.sortRecommended,
    SORT_FILTER.HIGH_TO_LOW to R.id.sortHighToLow,
    SORT_FILTER.LOW_TO_HIGH to R.id.sortLowToHigh,
    SORT_FILTER.NEWEST to R.id.sortNewest
)

class OffersFragment : Fragment(), CoroutineScope {
    private var campaigns: ArrayList<Campaign>? = null
    private lateinit var binding: OffersFragmentV2Binding
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private var sortFilter: SORT_FILTER = SORT_FILTER.NONE
    private var typeOfOffersFilter = ALL_OFFERS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            campaigns = it.getSerializable(OFFERS_PARAM) as ArrayList<Campaign>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.offers_fragment_v2, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        initSpinner()
//        initListeners()
        super.onViewCreated(view, savedInstanceState)
//        filteredCampaigns.addAll(campaigns!!)
        setupAdapter()
    }

//    fun initListeners() {
//        binding.sortFilters.setOnClickListener {
//            showOrderFilters()
//        }
//        binding.searchContainer.setOnClickListener {
//            val intent = Intent(requireActivity(), SearchOffersActivity::class.java)
//            intent.putExtra(CAMPAIGNS_PAYLOAD,campaigns)
//            startActivity(intent);
//            requireActivity().overridePendingTransition( R.anim.slide_in_up, android.R.anim.fade_out);
//        }
//    }



    fun setupAdapter() {
        binding.offersRecyclerV2.apply {
            adapter = campaigns?.let { OffersAdapterV2(it, activity as AppCompatActivity) }
        }
        //filterData()
//        binding.offersRecycler.apply {
//            adapter = OffersAdapterV2(filteredCampaigns, activity as AppCompatActivity)
//        }
    }

    fun filterData() {
        // filter by offer type
//        filteredCampaigns = when (typeOfOffersFilter) {
//            Constants.ALL_OFFERS -> campaigns!!
//            else -> campaigns!!.filter { it ->
//                Constants.ANDROID_CAMPAIGN_PARAM in it.oss
//            } as ArrayList<Campaign>
//        }

        // filter by sort
//        if (sortFilter !== SORT_FILTER.NONE) {
//            when (sortFilter) {
//                SORT_FILTER.HIGH_TO_LOW -> filteredCampaigns.sortWith(
//                    campaignHighToLowPayoutComparator
//                )
//                SORT_FILTER.LOW_TO_HIGH -> filteredCampaigns.sortWith(
//                    campaignLowToHighPayoutComparator
//                )
//                SORT_FILTER.RECOMMENDED -> filteredCampaigns.sortWith(campaignCrComparator)
//                else -> filteredCampaigns.sortByDescending { campaign -> dateFormatter.parse(campaign.createdAt) }
//            }
//        }
    }

    fun initSpinner() {
//+++++*******************************
    }


    fun showOrderFilters() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.sort_offers_bottom_sheet)

        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.sortGroup)
        if (sortFilter !== SORT_FILTER.NONE) {
            radioGroup?.check(SORT_FILTER_TO_ID[sortFilter]!!)
        }
        radioGroup?.setOnCheckedChangeListener { _, clickedId ->
            sortFilter = SORT_IDS_TO_SORT_FILTER[clickedId]!!
            setupAdapter()
        }
        bottomSheetDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ArrayList<Campaign>) =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(OFFERS_PARAM, param1)
                }
            }
    }
}

