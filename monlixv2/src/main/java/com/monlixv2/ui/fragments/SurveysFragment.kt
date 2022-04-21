package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.monlixv2.MonlixOffers
import com.monlixv2.R
import com.monlixv2.adapters.SurveysAdapter
import com.monlixv2.databinding.MonlixSurveysFragmentBinding
import com.monlixv2.service.models.campaigns.DEFAULT_LIMIT
import com.monlixv2.util.Constants
import com.monlixv2.util.RecyclerScrollListener
import com.monlixv2.viewmodels.SurveysViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext


class SurveysFragment : Fragment(), CoroutineScope {
    private lateinit var binding: MonlixSurveysFragmentBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private var surveysAdapter: SurveysAdapter? = null

    private var isLoadingFromDb = false
    private var isDbLastPage = false
    private var isScrollListenerAttached = false
    var currentOffset = 0

    private val surveysViewModel: SurveysViewModel by viewModels {
        Constants.viewModelFactory {
            SurveysViewModel(MonlixOffers.surveyRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.monlix_surveys_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (binding.surveyList.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if(position == 0)  2 else 1
            }
        }
        setupAdapter()
        loadData()

    }

    fun loadData() {
        lifecycleScope.launch {
            surveysViewModel.getSurveys(currentOffset).collect {

                if (it.isEmpty()) {
                    isDbLastPage = true
                    return@collect
                }

                if (surveysAdapter == null)
                    return@collect

                isLoadingFromDb = false
                isDbLastPage = false

                surveysAdapter?.appendData(it)

                if (!isScrollListenerAttached) {
                    attachScrollListener()
                }
            }
        }
    }


    private fun setupAdapter() {
        surveysAdapter = SurveysAdapter()
        binding.surveyList.adapter = surveysAdapter
    }

    private fun attachScrollListener() {
        isScrollListenerAttached = true
        binding.surveyList.addOnScrollListener(object :
            RecyclerScrollListener(binding.surveyList.layoutManager!! as GridLayoutManager) {
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
