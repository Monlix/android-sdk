package com.monlixv2

import android.app.Application
import android.content.Context
import com.monlixv2.service.models.ads.AdRepository
import com.monlixv2.service.localDb.OfferDatabase
import com.monlixv2.service.models.campaigns.CampaignRepository
import com.monlixv2.service.models.offers.OfferRepository
import com.monlixv2.service.models.surveys.SurveyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { OfferDatabase.getDatabase(this, applicationScope) }
    val adsRepository by lazy { AdRepository(database.adDao()) }
    val surveyRepository by lazy { SurveyRepository(database.surveyDao()) }
    val campaignsRepository by lazy { CampaignRepository(database.campaignDao()) }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
