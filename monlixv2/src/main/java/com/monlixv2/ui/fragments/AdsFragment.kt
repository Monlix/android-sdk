package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.AdsAdapter
import com.monlixv2.databinding.AdsFragmentBinding
import com.monlixv2.util.Constants
import com.monlixv2.viewmodels.AdsViewModel

class AdsFragment : Fragment() {
    private lateinit var binding: AdsFragmentBinding

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
        adsViewModel.allAds.observe(viewLifecycleOwner, Observer { ads ->
            binding.adRecycler.apply {
                adapter = AdsAdapter(ads)
            }
        })
    }
}
