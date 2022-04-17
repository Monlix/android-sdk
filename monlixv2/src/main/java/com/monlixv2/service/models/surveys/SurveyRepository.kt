package com.monlixv2.service.models.surveys

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SurveyRepository(private val surveyDao: SurveyDao) {
    val surveyCount: Flow<Int> = surveyDao.getSurveyCount()

    fun getSurveys(offset: Int): Flow<List<Survey>> {
        return surveyDao.getSurveys(offset);
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(offers: List<Survey>) {
        surveyDao.insertAllSurveys(offers)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        surveyDao.deleteAllSurveys()
    }

}
