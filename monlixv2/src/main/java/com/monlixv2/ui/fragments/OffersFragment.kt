package com.monlixv2.ui.fragments
import android.os.Bundle
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
import com.monlixv2.util.Constants.campaignCrComparator
import com.monlixv2.util.Constants.campaignDateComparator
import com.monlixv2.util.Constants.campaignHighToLowPayoutComparator
import com.monlixv2.util.Constants.campaignLowToHighPayoutComparator

private const val OFFERS_PARAM = "OFFERS_PARAM"
enum class SORT_FILTER {
    RECOMMENDED,
    HIGH_TO_LOW,
    LOW_TO_HIGH,
    NEWEST,
    NONE
}

val SORT_IDS_TO_SORT_FILTER = arrayMapOf<Int, SORT_FILTER>(
    R.id.sortRecommended to SORT_FILTER.RECOMMENDED,
    R.id.sortHighToLow to SORT_FILTER.HIGH_TO_LOW,
    R.id.sortLowToHigh to SORT_FILTER.LOW_TO_HIGH,
    R.id.sortNewest to SORT_FILTER.NEWEST,
)

class OffersFragment : Fragment() {
    private var campaigns: ArrayList<Campaign>? = null
    private var filteredCampaigns: ArrayList<Campaign>? = ArrayList()
    private lateinit var binding: OffersFragmentBinding
    private var sortFilter: SORT_FILTER = SORT_FILTER.NONE

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
        filteredCampaigns!!.addAll(campaigns!!)
        setupAdapter(filteredCampaigns!!)
        initSpinner()
        initListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initListeners(){
        binding.sortFilters.setOnClickListener {
            showOrderFilters()
        }
    }

    fun setupAdapter(campaignList: ArrayList<Campaign>) {
        binding.offersRecycler.apply {
            adapter =  OffersAdapter(campaignList)
        }
    }

    fun initSpinner(){
        binding.offerTypeSpinner.adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item,
            Constants.OFFER_FILTER_LIST
        )
        binding.offerTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                filteredCampaigns = when (adapterView.getItemAtPosition(i).toString()) {
                    Constants.ALL_OFFERS -> campaigns
                    else -> campaigns!!.filter { it -> Constants.ANDROID_CAMPAIGN_PARAM in it.oss
                    } as ArrayList<Campaign>
                }
                setupAdapter(filteredCampaigns!!)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
    }



    fun sortData(){
        when (sortFilter) {
            SORT_FILTER.HIGH_TO_LOW -> filteredCampaigns!!.sortWith(campaignHighToLowPayoutComparator)
            SORT_FILTER.LOW_TO_HIGH -> filteredCampaigns!!.sortWith(campaignLowToHighPayoutComparator)
            SORT_FILTER.RECOMMENDED -> filteredCampaigns!!.sortWith(campaignCrComparator)
            else -> filteredCampaigns!!.sortWith(campaignDateComparator)
        }
    }

    fun showOrderFilters() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.filter_bottom_sheet)

        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.sortGroup)
        radioGroup?.setOnCheckedChangeListener { _, clickedId ->
            sortFilter = SORT_IDS_TO_SORT_FILTER[clickedId]!!
            sortData()
            setupAdapter(filteredCampaigns!!)
        }
        bottomSheetDialog.show()
    }

    companion object {
        @JvmStatic fun newInstance(param1: ArrayList<Campaign>) =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(OFFERS_PARAM, param1)
                }
            }
    }
}
