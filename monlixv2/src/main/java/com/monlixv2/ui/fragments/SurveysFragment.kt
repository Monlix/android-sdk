package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.monlixv2.R
import com.monlixv2.databinding.SurveysFragmentBinding
import com.monlixv2.service.models.surveys.Survey

private const val SURVEYS_PARAM = "SURVEYS_PARAM"

class SurveysFragment : Fragment() {
    private var surveys: ArrayList<Survey>? = null
    private lateinit var binding: SurveysFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            surveys = it.getSerializable(SURVEYS_PARAM) as ArrayList<Survey>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.surveys_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("kreated")
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ArrayList<Survey>) =
            SurveysFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SURVEYS_PARAM, param1)
                }
            }
    }
}
