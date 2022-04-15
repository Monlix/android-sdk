package com.monlixv2.service.models.offers

import androidx.annotation.WorkerThread
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.service.models.offers.OfferDao
import kotlinx.coroutines.flow.Flow

class OfferRepository(private val offerDao: OfferDao) {
    val allOffers: Flow<List<Offer>> = offerDao.getOffers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(offers: ArrayList<Offer>) {
        offerDao.insertAll(offers)
    }
}
