package com.monlixv2.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.monlixv2.R
import com.monlixv2.adapters.AdsAdapter
import com.monlixv2.adapters.SurveysAdapter
import com.monlixv2.databinding.AdsFragmentBinding
import com.monlixv2.service.models.ads.Ad

private const val AD_PARAM = "AD_PARAM"

class AdsFragment : Fragment() {
    private var ads: ArrayList<Ad>? = null
    private lateinit var binding: AdsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ads = it.getSerializable(AD_PARAM) as ArrayList<Ad>?
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
        binding.adRecycler.apply {
            adapter = ads?.let { AdsAdapter(it) }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic fun newInstance(param1: ArrayList<Ad>) =
            AdsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(AD_PARAM, param1)
                }
            }
    }
}
