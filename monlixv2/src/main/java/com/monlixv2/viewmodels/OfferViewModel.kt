package com.monlixv2.viewmodels

import androidx.lifecycle.*
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.service.models.offers.OfferRepository
import kotlinx.coroutines.launch

class OfferViewModel(private val repository: OfferRepository) : ViewModel() {

    val allOffers: LiveData<List<Offer>> = repository.allOffers.asLiveData()

    fun insertAll(offerList: ArrayList<Offer>) = viewModelScope.launch {
        repository.insertAll(offerList)
    }
}
