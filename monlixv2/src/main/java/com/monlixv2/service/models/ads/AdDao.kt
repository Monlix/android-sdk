package com.monlixv2.service.models.ads

import androidx.room.*
import com.monlixv2.service.models.campaigns.CAMPAIGN_tABLE_NAME
import com.monlixv2.service.models.offers.Offer
import kotlinx.coroutines.flow.Flow

@Dao
interface AdDao {
    @Query("SELECT * FROM $AD_TABLE_NAME")
    fun getAllAds(): Flow<List<Ad>>

    @Query("SELECT count(id) FROM $AD_TABLE_NAME")
    fun getAdCount(): Flow<Int>

    @Query("DELETE FROM $AD_TABLE_NAME")
    suspend fun deleteAllAds()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAds(ads: List<Ad>)
}
