package com.monlixv2.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.collection.arrayMapOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapter
import com.monlixv2.databinding.OffersFragmentBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.ALL_OFFERS
import com.monlixv2.util.Constants.campaignCrComparator
import com.monlixv2.util.Constants.campaignHighToLowPayoutComparator
import com.monlixv2.util.Constants.campaignLowToHighPayoutComparator
import com.monlixv2.util.Constants.dateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var filteredCampaigns: ArrayList<Campaign> = ArrayList()
    private lateinit var binding: OffersFragmentBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private var sortFilter: SORT_FILTER = SORT_FILTER.NONE
    private var textFilter = ""
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
        binding = DataBindingUtil.inflate(inflater, R.layout.offers_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filteredCampaigns.addAll(campaigns!!)
        initSpinner()
        initListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initListeners() {
        binding.sortFilters.setOnClickListener {
            showOrderFilters()
        }
        binding.textSearch.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        private var searchFor = ""

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val searchText = s.toString().trim()
            if (searchText == searchFor)
                return

            searchFor = searchText

            launch {
                delay(300)  //debounce timeOut
                if (searchText != searchFor)
                    return@launch
                textFilter = searchFor.lowercase()
                setupAdapter()
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }

    fun setupAdapter() {
        filterData()
        binding.offersRecycler.apply {
            adapter = OffersAdapter(filteredCampaigns)
        }
    }

    fun filterData() {
        // filter by offer type
        filteredCampaigns = when (typeOfOffersFilter) {
            Constants.ALL_OFFERS -> campaigns!!
            else -> campaigns!!.filter { it ->
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

        // filter by text
        if (textFilter.isNotEmpty()) {
            filteredCampaigns = filteredCampaigns.filter { it ->
                it.name.lowercase().contains(textFilter)
            } as ArrayList<Campaign>
        }


    }

    fun initSpinner() {
        binding.offerTypeSpinner.adapter = ArrayAdapter<String>(
            requireContext(), R.layout.simple_spinner_dropdown_item,
            Constants.OFFER_FILTER_LIST
        )
        binding.offerTypeSpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                typeOfOffersFilter = adapterView.getItemAtPosition(i).toString()
                setupAdapter()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
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

