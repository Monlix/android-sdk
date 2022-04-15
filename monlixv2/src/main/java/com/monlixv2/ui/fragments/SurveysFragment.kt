package com.monlixv2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.SurveysAdapter
import com.monlixv2.databinding.SurveysFragmentBinding
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.util.Constants
import com.monlixv2.viewmodels.SurveysViewModel


class SurveysFragment : Fragment() {
    private lateinit var binding: SurveysFragmentBinding

    private val surveysViewModel: SurveysViewModel by viewModels {
        Constants.viewModelFactory {
            SurveysViewModel((context?.applicationContext as App).surveyRepository)
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
        super.onViewCreated(view, savedInstanceState)

        (binding.surveyList.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if(position == 0)  2 else 1
            }

        }
        surveysViewModel.allSurveys.observe(viewLifecycleOwner) {
            binding.surveyList.apply {
                adapter = it?.let { SurveysAdapter(it) }
            }
        }

    }

}
