package com.monlixv2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monlixv2.service.ApiInterface
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.service.models.surveys.Survey
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

class MainViewModel(APP_ID: String, USER_ID: String, application: Application) :
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
        resetResponse()
        _credentials.value = Credentials(APP_ID, USER_ID);
        makeRequest()
    }

    private fun resetResponse(){
        _groupedResponse.value = GroupedResponse(
            surveys = null,
            offers = null,
            campaigns = null,
            mergedSurveys = null
        )
    }

    fun initProgressBar() {
        resetProgressbar()
        setInterval(10) {
            val newCurrentProgress = _currentProgress.value!!.plus(step)
            _currentProgress.postValue(newCurrentProgress)
            _renderProgress.postValue(((atan(newCurrentProgress) / (Math.PI / 2) * 100 * 1000).roundToInt() / 1000))
        }
    }

    fun resetProgressbar(){
        _renderProgress.value = 0
        _currentProgress.value = 0.0
    }

    fun surveysRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getSurveys(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                _groupedResponse.value?.surveys = response.body()
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception ${e.message}")
            }
        }
    }

    fun offersRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getOffers(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                _groupedResponse.value?.offers = response.body()
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception ${e.message}")
            }
        }
    }

    fun campaignsRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getCampaigns(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                val campaigns = if(response.body() != null) response.body() else ArrayList();
                //remove ios campaigns
                val filtered = campaigns!!.filter {
                        el -> IOS_CAMPAIGN_PARAM !in el.oss
                } as ArrayList<Campaign>
                _groupedResponse.value?.campaigns =  filtered
                checkProgress()
            } catch (e: Exception) {
                println("Monlix Exception ${e.message}")
            }
        }
    }

    fun makeRequest() {
        resetResponse()
        _isLoading.value = true
        initProgressBar()
        surveysRequest();
        offersRequest();
        campaignsRequest()
    }

    fun setInterval(timeMillis: Long, handler: () -> Unit) = coroutineScope.launch {
        while (_isLoading.value!!) {
            delay(timeMillis)
            handler()
        }
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
            _isLoading.postValue(false)
            resetProgressbar()
        }
    }

}
