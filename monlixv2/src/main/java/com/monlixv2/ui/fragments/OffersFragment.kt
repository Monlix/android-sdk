package com.monlixv2.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.monlixv2.R
import com.monlixv2.adapters.AdsAdapter
import com.monlixv2.adapters.OffersAdapter
import com.monlixv2.databinding.OffersFragmentBinding
import com.monlixv2.databinding.SurveysFragmentBinding
import com.monlixv2.service.models.campaigns.Campaign

private const val OFFERS_PARAM = "OFFERS_PARAM"

class OffersFragment : Fragment() {
    private var campaigns: ArrayList<Campaign>? = null
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
        binding.offersRecycler.apply {
            adapter = campaigns?.let { OffersAdapter(it) }
        }
        super.onViewCreated(view, savedInstanceState)
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
