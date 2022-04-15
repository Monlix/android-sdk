package com.monlixv2.viewmodels

import androidx.lifecycle.*
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.campaigns.CampaignRepository
import com.monlixv2.service.models.campaigns.PLATFORM_ALL
import com.monlixv2.util.Constants
import kotlinx.coroutines.launch

class CampaignsViewModel(private val repository: CampaignRepository) : ViewModel() {

    val allCampaigns: LiveData<List<Campaign>> = repository.allCampaigns.asLiveData()

    val campaignsCount: LiveData<Int> = repository.campaignCount.asLiveData()

    fun getSortedCampaigns(platform: Int, sortType: Constants.SORT_FILTER): LiveData<List<Campaign>> {
        return repository.sortedCampaigns(platform, sortType).asLiveData()
    }


    fun insertAll(offerList: List<Campaign>) = viewModelScope.launch {
        repository.insertAll(offerList)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
