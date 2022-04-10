package com.monlixv2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.monlixv2.service.models.transactions.Transaction
import com.monlixv2.service.ApiInterface
import com.monlixv2.util.Credentials
import com.monlixv2.util.Constants.ALL_ACTIVITY
import com.monlixv2.util.Constants.TRANSACTION_FILTER_QUERY_PARAM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException


class TransactionsViewModel(APP_ID: String, USER_ID: String, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _transactionList = MutableLiveData<ArrayList<Transaction>>()
    val transactionList: LiveData<ArrayList<Transaction>>
        get() = _transactionList

    private val _points = MutableLiveData<String>("0")
    val points: LiveData<String>
        get() = _points

    private val _clicks = MutableLiveData<String>("0")
    val clicks: LiveData<String>
        get() = _clicks

    private val _pageId = MutableLiveData<String>("")
    val pageId: LiveData<String>
        get() = _pageId

    private val _statusFilter = MutableLiveData<String>(ALL_ACTIVITY)
    val statusFilter: LiveData<String>
        get() = _statusFilter


    private val _showNoData = MutableLiveData<Boolean>(false)
    val showNoData: LiveData<Boolean>
        get() = _showNoData

    fun setStatusFilter(filter: String){
        _statusFilter.value = filter
    }

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _credentials = MutableLiveData<Credentials>(null)
    val credentials: LiveData<Credentials>
        get() = _credentials

    init {
        _credentials.value = Credentials(APP_ID, USER_ID);
    }

    fun resetData() {
        if(_transactionList.value !== null && _transactionList.value!!.size > 0) {
            _transactionList.value = arrayListOf()
        }
    }

    fun getTransactions() {
        _showNoData.value = false;
        _isLoading.value = true;

        coroutineScope.launch {
            val response = ApiInterface.getInstance().getTransactions(
                _credentials.value!!.appId,
                _credentials.value!!.userId,
                "",
                _pageId.value!!,
                TRANSACTION_FILTER_QUERY_PARAM[_statusFilter.value]!!
            )
            try {
                _isLoading.postValue(false)
                if (response.body()?.transactions?.size == 0) {
                    _showNoData.postValue(true)
                    _isLoading.postValue(false)
                }
                _transactionList.postValue(response.body()?.transactions ?: ArrayList())
                _points.postValue(response.body()?.ptcEarnings.toString())
                _clicks.postValue(response.body()?.ptcClicks.toString())
            } catch (e: HttpException) {
                println("Exception ${e.message}")
            } catch (e: Throwable) {
                println(e)
            }
        }

    }

}
