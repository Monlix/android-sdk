package com.monlixv2.viewmodels

import androidx.lifecycle.*
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdRepository
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.surveys.SurveyRepository
import kotlinx.coroutines.launch

class SurveysViewModel(private val repository: SurveyRepository) : ViewModel() {

    val allSurveys: LiveData<List<Survey>> = repository.allSurveys.asLiveData()
    val surveyCount: LiveData<Int> = repository.surveyCount.asLiveData()

    init {
        println("INITED")
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(surveys: List<Survey>) = viewModelScope.launch {
        repository.insertAll(surveys)
    }

    fun insertAll(surveys: List<Survey>) = viewModelScope.launch {
        repository.insertAll(surveys)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
