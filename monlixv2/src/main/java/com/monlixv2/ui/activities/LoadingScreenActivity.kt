package com.monlixv2.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.monlixv2.App
import com.monlixv2.R
import com.monlixv2.adapters.AD_FRAGMENT
import com.monlixv2.adapters.OFFER_FRAGMENT
import com.monlixv2.adapters.SURVEY_FRAGMENT
import com.monlixv2.databinding.LoadingScreenBinding
import com.monlixv2.util.Constants.AVAILABLE_FRAGMENTS
import com.monlixv2.util.Constants.viewModelFactory
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.get
import com.monlixv2.viewmodels.AdsViewModel
import com.monlixv2.viewmodels.CampaignsViewModel
import com.monlixv2.viewmodels.LoadingViewModel
import com.monlixv2.viewmodels.SurveysViewModel

const val EMPTY_FRAGMENT_SIZE = -1

class LoadingScreenActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var viewModel: LoadingViewModel
    private lateinit var binding: LoadingScreenBinding
    private var fragmentsToShow = mutableListOf<Int>()
    private var numberOfFragments : Int = EMPTY_FRAGMENT_SIZE
    private var isFetchedNetworkData = false

    private val campaignsViewModel: CampaignsViewModel by viewModels {
        viewModelFactory {
            CampaignsViewModel((application as App).campaignsRepository)
        }
    }
    private val adsViewModel: AdsViewModel by viewModels {
        viewModelFactory {
            AdsViewModel((application as App).adsRepository)
        }
    }
    private val surveysViewModel: SurveysViewModel by viewModels {
        viewModelFactory {
            SurveysViewModel((application as App).surveyRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clearDB()

        binding = DataBindingUtil.setContentView(this, R.layout.loading_screen)
        binding.lifecycleOwner = this
        prefs = PreferenceHelper.customPrefs(this, PreferenceHelper.MonlixPrefs);
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory {
                    LoadingViewModel(
                        prefs[MonlixAppId, ""]!!,
                        prefs[MonlixUserId, ""]!!,
                        application
                    )
                }).get(
                LoadingViewModel::class.java
            )
        binding.viewModel = viewModel
        setupListeners()
    }

    private fun clearDB() {
        campaignsViewModel.deleteAll()
        adsViewModel.deleteAll()
        surveysViewModel.deleteAll()
    }

    private fun setupListeners() {

        campaignsViewModel.campaignsCount.observe(this) {
            if(isFetchedNetworkData && it > 0) {
                makeFragmentVisible(OFFER_FRAGMENT)
            }
        }
        adsViewModel.adCount.observe(this) {
            if(isFetchedNetworkData && it > 0) {
                makeFragmentVisible(AD_FRAGMENT)
            }
        }

        surveysViewModel.surveyCount.observe(this) {
            if(isFetchedNetworkData && it > 0) {
                makeFragmentVisible(SURVEY_FRAGMENT)
            }
        }
        viewModel.groupedResponse.observe(this, Observer {
            if (it.mergedSurveys !== null) {
                isFetchedNetworkData = true
                numberOfFragments = 0
                if(it.mergedSurveys!!.size > 0) {
                    numberOfFragments += 1
                    surveysViewModel.insertAll(it.mergedSurveys!!)
                }
                if(it.offers!!.ads.size > 0) {
                    numberOfFragments += 1
                    adsViewModel.insertAll(it.offers!!.ads)
                }
                if(it.campaigns!!.size > 0) {
                    numberOfFragments += 1
                    campaignsViewModel.insertAll(it.campaigns!!)
                }
            }
        })
    }

    private fun makeFragmentVisible(fragment: Int) {
        fragmentsToShow.add(fragment)
        if(fragmentsToShow.size == numberOfFragments) {
            viewModel.finishAnimation()
            goToMainScreen()
        }
    }

    private fun goToMainScreen(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(AVAILABLE_FRAGMENTS, fragmentsToShow.toIntArray())
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

}
