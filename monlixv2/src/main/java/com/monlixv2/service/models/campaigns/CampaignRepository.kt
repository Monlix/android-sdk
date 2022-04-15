package com.monlixv2.service.models.campaigns

import androidx.annotation.WorkerThread
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdDao
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.service.models.offers.OfferDao
import com.monlixv2.util.Constants
import kotlinx.coroutines.flow.Flow

class CampaignRepository(private val campaignDao: CampaignDao) {
    val allCampaigns: Flow<List<Campaign>> = campaignDao.getAllCampaigns()

    fun sortedCampaigns(platform: Int, sortType: Constants.SORT_FILTER): Flow<List<Campaign>> {
        return when (sortType) {
            Constants.SORT_FILTER.HIGH_TO_LOW -> campaignDao.getSortedCampaignsByHighToLow(platform)
            Constants.SORT_FILTER.LOW_TO_HIGH -> campaignDao.getSortedCampaignsByLowToHigh(platform)
            Constants.SORT_FILTER.RECOMMENDED -> campaignDao.getSortedCampaignsByRecommended(platform)
            Constants.SORT_FILTER.NEWEST -> campaignDao.getSortedCampaignsByDate(platform)
            else -> campaignDao.getAllCampaigns()
        }
    }

    val campaignCount: Flow<Int> = campaignDao.getCampaignsCount()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(campaigns: List<Campaign>) {
        campaignDao.insertCampaigns(campaigns)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        campaignDao.deleteAllCampaigns()
    }
}
