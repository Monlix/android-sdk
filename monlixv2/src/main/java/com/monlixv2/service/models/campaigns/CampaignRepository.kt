package com.monlixv2.service.models.campaigns

import androidx.annotation.WorkerThread
import com.monlixv2.util.Constants
import kotlinx.coroutines.flow.Flow

class CampaignRepository(private val campaignDao: CampaignDao) {
    val allCampaigns: Flow<List<Campaign>> = campaignDao.getAllCampaigns(PLATFORM_ALL, 0)
    val featuredCampaigns: Flow<List<Campaign>> = campaignDao.getFeaturedCampaigns();

    fun searchCampaignsByTitle(title: String): Flow<List<Campaign>> {
        return campaignDao.searchCampaignsByTitle(title)
    }

    fun sortedCampaigns(platform: Int, sortType: Constants.SORT_FILTER, offset: Int): Flow<List<Campaign>> {
        return when (sortType) {
            Constants.SORT_FILTER.HIGH_TO_LOW -> campaignDao.getSortedCampaignsByHighToLow(platform, offset)
            Constants.SORT_FILTER.LOW_TO_HIGH -> campaignDao.getSortedCampaignsByLowToHigh(platform, offset)
            Constants.SORT_FILTER.RECOMMENDED -> campaignDao.getSortedCampaignsByRecommended(platform, offset)
            Constants.SORT_FILTER.NEWEST -> campaignDao.getSortedCampaignsByDate(platform, offset)
            else -> campaignDao.getAllCampaigns(platform, offset)
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
