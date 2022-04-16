package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.OffersAdapterV2
import com.monlixv2.databinding.OffersFragmentV2Binding
import com.monlixv2.util.Constants
import com.monlixv2.util.RecyclerScrollListener
import com.monlixv2.viewmodels.CampaignsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class OffersFragment : Fragment(), CoroutineScope {
    private lateinit var binding: OffersFragmentV2Binding
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private var offersAdapterV2: OffersAdapterV2? = null
    private var isLoadingFromDb = false
    private var isDbLastPage = false
    private var isScrollListenerAttached = false

    private val campaignsViewModel: CampaignsViewModel by viewModels {
        Constants.viewModelFactory {
            CampaignsViewModel((context?.applicationContext as App).campaignsRepository)
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


    fun filterData(platform: Int, sortType: Constants.SORT_FILTER, offset: Int = 0) {
        launch {
            campaignsViewModel.getSortedCampaigns(platform, sortType, offset).collect {
                if (it.isEmpty()) {
                    isDbLastPage = true
                    return@collect
                }

                if (offersAdapterV2 == null)
                    return@collect

                isLoadingFromDb = false
                isDbLastPage = false

                if (offersAdapterV2?.currentOffset != 0) {
                    offersAdapterV2?.appendData(it)
                } else {
                    offersAdapterV2?.replaceData(it)
                }

                if (!isScrollListenerAttached) {
                    attachScrollListener()
                }
            }
        }
    }


    private fun attachScrollListener() {
        isScrollListenerAttached = true
        binding.offersRecyclerV2.addOnScrollListener(object :
            RecyclerScrollListener(binding.offersRecyclerV2.layoutManager!! as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoadingFromDb = true
                offersAdapterV2?.loadNextPage()
            }

            override val isLastPage: Boolean
                get() = isDbLastPage
            override val isLoading: Boolean
                get() = isLoadingFromDb
        })
    }

    private fun setupAdapter() {
        offersAdapterV2 = OffersAdapterV2(context as AppCompatActivity, this@OffersFragment)
        binding.offersRecyclerV2.adapter = offersAdapterV2
    }

    override fun onPause() {
        super.onPause()
        println("GOING TO PAUSE")
    }

}

