package com.monlixv2.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.monlixv2.R
import com.monlixv2.adapters.TransactionAdapter
import com.monlixv2.databinding.TransactionFragmentBinding
import com.monlixv2.ui.activities.Main
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.TRANSACTION_FILTER_LIST
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.get
import com.monlixv2.util.RecyclerScrollListener
import com.monlixv2.util.UIHelpers
import com.monlixv2.viewmodels.TransactionsViewModel


class TransactionFragment : Fragment() {

    private lateinit var prefs: SharedPreferences

    private lateinit var viewModel: TransactionsViewModel
    private var binding: TransactionFragmentBinding? = null

    private var isRequestLoading = false
    private var isRequestLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prefs = PreferenceHelper.customPrefs(context as Main, PreferenceHelper.MonlixPrefs);
        binding = TransactionFragmentBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        viewModel =
            ViewModelProvider(
                this,
                Constants.viewModelFactory { TransactionsViewModel(prefs[PreferenceHelper.MonlixAppId,""]!!,prefs[PreferenceHelper.MonlixUserId, ""]!!, requireActivity().application) }).get(
                TransactionsViewModel::class.java
            )
        binding!!.viewModel = viewModel
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        initSpinner()
        bindListeners()
    }

    private fun setupRecyclerView() {
        context?.resources?.getDimensionPixelSize(R.dimen.offer_row_margin)?.let {
            UIHelpers.MarginItemDecoration(
                it
            )
        }?.let {
            binding?.offerRecycler?.addItemDecoration(
                it
            )
        }

        binding?.offerRecycler?.addOnScrollListener(object : RecyclerScrollListener(binding!!.offerRecycler.layoutManager!! as LinearLayoutManager){
            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }
            override val isLastPage: Boolean
                get() = isRequestLastPage
            override val isLoading: Boolean
                get() = isRequestLoading
        })


    }

    fun initSpinner(){
        binding?.spinner?.adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item,TRANSACTION_FILTER_LIST)
        binding?.spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val value = adapterView.getItemAtPosition(i).toString()
                viewModel.resetData()
                viewModel.setStatusFilter(value)
                viewModel.getTransactions()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
    }

    fun bindListeners() {
        viewModel.transactionList.observe(viewLifecycleOwner) {
            if (!viewModel.pageId.value.contentEquals("0")) {
                (binding?.offerRecycler?.adapter as TransactionAdapter).updateDataSource(it)
            } else {
                binding?.offerRecycler?.adapter = TransactionAdapter(it);
            }
        }
        viewModel.isLastPage.observe(viewLifecycleOwner) {
            isRequestLastPage = it
        }
        viewModel.isLoadingRequest.observe(viewLifecycleOwner) {
            isRequestLoading = it
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
