package com.monlixv2.viewmodels

import androidx.lifecycle.*
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.surveys.SurveyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SurveysViewModel(private val repository: SurveyRepository) : ViewModel() {

    fun getSurveys(offset: Int): Flow<List<Survey>> {
        return repository.getSurveys(offset)
    }

    val surveyCount: LiveData<Int> = repository.surveyCount.asLiveData()

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
