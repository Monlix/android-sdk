package com.monlixv2.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapter
import com.monlixv2.databinding.OffersFragmentBinding
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.util.Constants

private const val OFFERS_PARAM = "OFFERS_PARAM"

class OffersFragment : Fragment() {
    private var campaigns: ArrayList<Campaign>? = null
    private var filteredCampaigns: ArrayList<Campaign>? = ArrayList()
    private lateinit var binding: OffersFragmentBinding

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
        super.onViewCreated(view, savedInstanceState)
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

                println("WAAAAAAAAAAAA ${adapterView.getItemAtPosition(i).toString()}")
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

    companion object {
        @JvmStatic fun newInstance(param1: ArrayList<Campaign>) =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(OFFERS_PARAM, param1)
                }
            }
    }
}
