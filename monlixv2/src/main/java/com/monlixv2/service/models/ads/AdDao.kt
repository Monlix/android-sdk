package com.monlixv2.service.models.ads

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monlixv2.service.models.campaigns.DEFAULT_LIMIT
import kotlinx.coroutines.flow.Flow

@Dao
interface AdDao {
    @Query("SELECT * FROM $AD_TABLE_NAME limit $DEFAULT_LIMIT offset :offset")
    fun getAllAds(offset: Int): Flow<List<Ad>>

    @Query("SELECT count(id) FROM $AD_TABLE_NAME")
    fun getAdCount(): Flow<Int>

    @Query("DELETE FROM $AD_TABLE_NAME")
    suspend fun deleteAllAds()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAds(ads: List<Ad>)
}
