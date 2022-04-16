package com.monlixv2.service.models.ads

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AdRepository(private val adDao: AdDao) {

    fun getAds(offset: Int): Flow<List<Ad>> {
        return adDao.getAllAds(offset);
    }

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
