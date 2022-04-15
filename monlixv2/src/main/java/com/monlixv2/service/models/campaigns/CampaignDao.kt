package com.monlixv2.service.models.campaigns

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {
    @Query("SELECT * FROM $CAMPAIGN_tABLE_NAME")
    fun getAllCampaigns(): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform = :platform order by payout DESC ")
    fun getSortedCampaignsByHighToLow(platform: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform = :platform order by payout ASC ")
    fun getSortedCampaignsByLowToHigh(platform: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform = :platform order by cr ASC ")
    fun getSortedCampaignsByRecommended(platform: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform = :platform order by date(createdAt) DESC ")
    fun getSortedCampaignsByDate(platform: Int): Flow<List<Campaign>>

    @Query("SELECT count(id) FROM $CAMPAIGN_tABLE_NAME")
    fun getCampaignsCount(): Flow<Int>

    @Query("DELETE FROM $CAMPAIGN_tABLE_NAME")
    suspend fun deleteAllCampaigns()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCampaigns(campaigns: List<Campaign>)
}
