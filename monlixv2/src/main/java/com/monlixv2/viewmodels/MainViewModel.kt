package com.monlixv2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monlixv2.service.ApiInterface
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.get
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

data class Credentials (
    val appId: String,
    val userId: String
)

class MainViewModel(APP_ID: String, USER_ID: String, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _credentials = MutableLiveData<Credentials>(null)
    val credentials: LiveData<Credentials>
        get() = _credentials

    init {
        _credentials.value = Credentials(APP_ID, USER_ID);
        makeRequest();
    }


    fun makeRequest() {
        println("making request")
        println(_credentials.value)
        try {
            coroutineScope.launch {
                val response = ApiInterface.getInstance().getTransactions(_credentials.value!!.appId, _credentials.value!!.userId,"","","")
                try {
                    println("response")
                    println(response.body()?.transactions?.size)
                    _isLoading.postValue(false)
                } catch (e: HttpException) {
                    println("Exception ${e.message}")
                } catch (e: Throwable) {
                    println(e)
                    println("Ooops: Something else went wrong")
                }
            }
        }catch (e: Exception) {
            println(e)
        }

    }

}
