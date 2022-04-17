package com.monlixv2.service.models.surveys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monlixv2.service.models.campaigns.DEFAULT_LIMIT
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Query("SELECT * FROM $SURVEY_TABLE_NAME limit $DEFAULT_LIMIT offset :offset")
    fun getSurveys(offset: Int): Flow<List<Survey>>

    @Query("SELECT count(id) FROM $SURVEY_TABLE_NAME")
    fun getSurveyCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllSurveys(offers: List<Survey>)

    @Query("DELETE FROM survey")
    suspend fun deleteAllSurveys()
}
