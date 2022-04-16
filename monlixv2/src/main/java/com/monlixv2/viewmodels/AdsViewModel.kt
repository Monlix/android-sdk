package com.monlixv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdRepository) : ViewModel() {


    fun getAds(offset: Int): Flow<List<Ad>> {
        return repository.getAds(offset)
    }


    val adCount: LiveData<Int> = repository.adCount.asLiveData()


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
