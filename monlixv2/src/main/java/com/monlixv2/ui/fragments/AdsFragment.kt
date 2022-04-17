package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.AdsAdapter
import com.monlixv2.databinding.AdsFragmentBinding
import com.monlixv2.service.models.campaigns.DEFAULT_LIMIT
import com.monlixv2.util.Constants
import com.monlixv2.util.RecyclerScrollListener
import com.monlixv2.viewmodels.AdsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AdsFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private lateinit var binding: AdsFragmentBinding
    private var adsAdapter: AdsAdapter? = null
    private var isLoadingFromDb = false
    private var isDbLastPage = false
    private var isScrollListenerAttached = false
    var currentOffset = 0

    private val adsViewModel: AdsViewModel by viewModels {
        Constants.viewModelFactory {
            AdsViewModel((context?.applicationContext as App).adsRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.ads_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        loadData()
    }

    fun loadData() {
        lifecycleScope.launch {
            adsViewModel.getAds(currentOffset).collect {

                if (it.isEmpty()) {
                    isDbLastPage = true
                    return@collect
                }

                if (adsAdapter == null)
                    return@collect

                isLoadingFromDb = false
                isDbLastPage = false

                adsAdapter?.appendData(it)

                if (!isScrollListenerAttached) {
                    attachScrollListener()
                }
            }
        }
    }


    private fun setupAdapter() {
        adsAdapter = AdsAdapter()
        binding.adRecycler.adapter = adsAdapter
    }

    private fun attachScrollListener() {
        isScrollListenerAttached = true
        binding.adRecycler.addOnScrollListener(object :
            RecyclerScrollListener(binding.adRecycler.layoutManager!! as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoadingFromDb = true
                currentOffset += DEFAULT_LIMIT
                loadData()
            }

            override val isLastPage: Boolean
                get() = isDbLastPage
            override val isLoading: Boolean
                get() = isLoadingFromDb
        })
    }
}
