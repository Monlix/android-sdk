package com.monlixv2.viewmodels

import androidx.lifecycle.*
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdRepository
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdRepository) : ViewModel() {

    val allAds: LiveData<List<Ad>> = repository.allAds.asLiveData()

    val adCount: LiveData<Int> = repository.adCount.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(ads: List<Ad>) = viewModelScope.launch {
        repository.insertAll(ads)
    }

    fun insertAll(adList: ArrayList<Ad>) = viewModelScope.launch {
        repository.insertAll(adList)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
