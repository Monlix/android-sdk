package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapterV2
import com.monlixv2.databinding.OffersFragmentV2Binding
import com.monlixv2.service.models.campaigns.Campaign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


private const val OFFERS_PARAM = "OFFERS_PARAM"



class OffersFragment : Fragment(), CoroutineScope {
    private var campaigns: ArrayList<Campaign>? = null
    private lateinit var binding: OffersFragmentV2Binding
    override val coroutineContext: CoroutineContext = Dispatchers.Main


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
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }



    private fun setupAdapter() {
        binding.offersRecyclerV2.apply {
            adapter = campaigns?.let { OffersAdapterV2(it, activity as AppCompatActivity) }
        }
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

