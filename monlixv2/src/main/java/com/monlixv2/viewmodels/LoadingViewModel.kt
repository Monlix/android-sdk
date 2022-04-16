package com.monlixv2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monlixv2.service.ApiInterface
import com.monlixv2.service.ApiTest
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.campaigns.PLATFORM_ALL
import com.monlixv2.service.models.campaigns.PLATFORM_ANDROID
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.util.Constants
import com.monlixv2.util.Constants.IOS_CAMPAIGN_PARAM
import com.monlixv2.util.Credentials
import kotlinx.coroutines.*
import kotlin.math.atan
import kotlin.math.roundToInt

data class GroupedResponse(
    var surveys: ArrayList<Survey>?,
    var offers: OfferResponse?,
    var campaigns: ArrayList<Campaign>?,
    var mergedSurveys: ArrayList<Survey>?,
)
const val step = 0.3

class LoadingViewModel(APP_ID: String, USER_ID: String, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _groupedResponse = MutableLiveData<GroupedResponse>()
    val groupedResponse: LiveData<GroupedResponse>
        get() = _groupedResponse

    private val _renderProgress = MutableLiveData<Int>(0)
    val renderProgress: LiveData<Int>
        get() = _renderProgress

    private val _currentProgress = MutableLiveData<Double>(0.0)
    val currentProgress: LiveData<Double>
        get() = _currentProgress


    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _credentials = MutableLiveData<Credentials>(null)
    val credentials: LiveData<Credentials>
        get() = _credentials

    init {
        _groupedResponse.value = GroupedResponse(
            surveys = null,
            offers = null,
            campaigns = null,
            mergedSurveys = null
        )
        _credentials.value = Credentials(APP_ID, USER_ID);
        initProgressBar()
        makeRequests()
    }

    private fun initProgressBar() {
        setInterval(10) {
            val newCurrentProgress = _currentProgress.value!!.plus(step)
            _currentProgress.postValue(newCurrentProgress)
            _renderProgress.postValue(((atan(newCurrentProgress) / (Math.PI / 2) * 100 * 1000).roundToInt() / 1000))
        }
    }

    private fun surveysRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getSurveys(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                _groupedResponse.value?.surveys = response.body()
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception surveys -${e.message}")
            }
        }
    }

    private fun offersRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getOffers(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                _groupedResponse.value?.offers = response.body()
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception offers - ${e.message}")
            }
        }
    }

    private fun campaignsRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getCampaigns(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                val campaigns = if(response.body() != null) response.body() else ArrayList();
                val filtered = campaigns!!.filter {
                        el -> IOS_CAMPAIGN_PARAM !in el.oss
                } as ArrayList<Campaign>
                for (el in filtered) {
                    el.platform = if(el.oss.contains(Constants.ANDROID_CAMPAIGN_PARAM)) PLATFORM_ANDROID else PLATFORM_ALL
                }
                 _groupedResponse.value?.campaigns =  filtered
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception campaigns -${e.message}")
            }
        }
    }

    private fun makeRequests() {
        surveysRequest();
        offersRequest();
        campaignsRequest()
    }

    private fun setInterval(timeMillis: Long, handler: () -> Unit) = coroutineScope.launch {
        while (_isLoading.value!!) {
            delay(timeMillis)
            handler()
        }
    }

    fun finishAnimation(){
        _isLoading.postValue(false)
    }

    fun checkProgress() {
        if (_groupedResponse.value?.campaigns !== null && _groupedResponse.value?.offers !== null && _groupedResponse.value?.surveys !== null) {

            val merged = ArrayList<Survey>()
            merged.addAll(_groupedResponse.value!!.surveys!!)
            merged.addAll(_groupedResponse.value!!.offers!!.surveys)

            val newGroupedResponse = GroupedResponse(
                surveys = _groupedResponse.value!!.surveys,
                offers = _groupedResponse.value!!.offers,
                campaigns = _groupedResponse.value!!.campaigns,
                mergedSurveys = merged
            )

            _groupedResponse.value = newGroupedResponse
        }
    }

}
