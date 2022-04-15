package com.monlixv2.service.models.offers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Query("SELECT * FROM OFFER")
    fun getOffers(): Flow<List<Offer>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(offers: List<Offer>)

    @Query("DELETE FROM OFFER")
    suspend fun deleteAll()
}
