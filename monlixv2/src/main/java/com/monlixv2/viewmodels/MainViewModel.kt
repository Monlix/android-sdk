package com.monlixv2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monlixv2.service.ApiInterface
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.util.Credentials
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

data class GroupedResponse(
    var surveys: ArrayList<Survey>?,
    var offers: OfferResponse?,
    var campaigns: ArrayList<Campaign>?
)

class MainViewModel(APP_ID: String, USER_ID: String, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _groupedResponse = MutableLiveData<GroupedResponse>()
    val groupedResponse: LiveData<GroupedResponse>
        get() = _groupedResponse


    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _credentials = MutableLiveData<Credentials>(null)
    val credentials: LiveData<Credentials>
        get() = _credentials

    init {
        _credentials.value = Credentials(APP_ID, USER_ID);
        makeRequest()
    }

    fun surveysRequest() {
        coroutineScope.launch {
            val response = ApiInterface.getInstance()
                .getSurveys(_credentials.value!!.appId, _credentials.value!!.userId, "")
            try {
                println("response surveys")
                _groupedResponse.value?.surveys = response.body()
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
                println("campaigns offers")
                _groupedResponse.value?.campaigns = response.body()
            } catch (e: Exception) {
                println("Monlix Exception ${e.message}")
            }
        }
    }

    fun makeRequest() {
        _isLoading.value = true
        surveysRequest();
        offersRequest();
        campaignsRequest()
    }

}
