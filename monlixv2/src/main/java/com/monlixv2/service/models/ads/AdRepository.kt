package com.monlixv2.service.models.ads

import androidx.annotation.WorkerThread
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdDao
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.service.models.offers.OfferDao
import kotlinx.coroutines.flow.Flow

class AdRepository(private val adDao: AdDao) {
    val allAds: Flow<List<Ad>> = adDao.getAllAds()
    val adCount: Flow<Int> = adDao.getAdCount()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(ads: List<Ad>) {
        adDao.insertAllAds(ads)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        adDao.deleteAllAds()
    }
}
