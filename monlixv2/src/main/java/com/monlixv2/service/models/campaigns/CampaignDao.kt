package com.monlixv2.service.models.campaigns

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

const val DEFAULT_LIMIT = 30

@Dao
interface CampaignDao {
    @Query("SELECT * FROM $CAMPAIGN_tABLE_NAME where platform <= :platform limit $DEFAULT_LIMIT offset :offset")
    fun getAllCampaigns(platform: Int, offset: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM $CAMPAIGN_tABLE_NAME where featured = 1")
    fun getFeaturedCampaigns(): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform <= :platform order by payout DESC limit $DEFAULT_LIMIT offset :offset")
    fun getSortedCampaignsByHighToLow(platform: Int, offset: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform <= :platform order by payout ASC limit $DEFAULT_LIMIT offset :offset")
    fun getSortedCampaignsByLowToHigh(platform: Int, offset: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform <= :platform order by cr ASC limit $DEFAULT_LIMIT offset :offset")
    fun getSortedCampaignsByRecommended(platform: Int, offset: Int): Flow<List<Campaign>>

    @Query("SELECT * FROM campaign where platform <= :platform order by date(createdAt) DESC limit $DEFAULT_LIMIT offset :offset")
    fun getSortedCampaignsByDate(platform: Int, offset: Int): Flow<List<Campaign>>

    @Query("SELECT count(id) FROM $CAMPAIGN_tABLE_NAME")
    fun getCampaignsCount(): Flow<Int>

    @Query("DELETE FROM $CAMPAIGN_tABLE_NAME")
    suspend fun deleteAllCampaigns()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCampaigns(campaigns: List<Campaign>)

    @Query("SELECT * FROM $CAMPAIGN_tABLE_NAME where lower(name) LIKE '%' || :name || '%' ")
    fun searchCampaignsByTitle(name: String): Flow<List<Campaign>>
}
