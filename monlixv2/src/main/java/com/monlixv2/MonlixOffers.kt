package com.monlixv2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.monlixv2.service.localDb.OfferDatabase
import com.monlixv2.service.models.ads.AdRepository
import com.monlixv2.service.models.campaigns.CampaignRepository
import com.monlixv2.service.models.surveys.SurveyRepository
import com.monlixv2.ui.activities.LoadingScreenActivity
import com.monlixv2.util.PreferenceHelper
import com.monlixv2.util.PreferenceHelper.MonlixAppId
import com.monlixv2.util.PreferenceHelper.MonlixPrefs
import com.monlixv2.util.PreferenceHelper.MonlixUserId
import com.monlixv2.util.PreferenceHelper.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


object MonlixOffers {
    private var instance: MonlixOffers? = null
    private lateinit var prefs: SharedPreferences

    private val applicationScope = CoroutineScope(SupervisorJob())
    private lateinit var database: OfferDatabase;
    val adsRepository by lazy { AdRepository(database.adDao()) }
    val surveyRepository by lazy { SurveyRepository(database.surveyDao()) }
    val campaignsRepository by lazy { CampaignRepository(database.campaignDao()) }

    @Synchronized
    fun createInstance(
        context: Context,
        appId: String,
        userId: String
    ): MonlixOffers? {
        if (instance == null) {
            database =  OfferDatabase.getDatabase(context, applicationScope)
            instance = MonlixOffers
            prefs = PreferenceHelper.customPrefs(context, MonlixPrefs);
            prefs[MonlixAppId] = appId
            prefs[MonlixUserId] = userId
        }
        return instance;
    }


    fun showWall(context: Context) {
        if (instance == null) {
            throw Exception("createInstance() must be called before accessing the instance.")
        }
        val intent = Intent(context, LoadingScreenActivity::class.java)
        context.startActivity(intent)
    }

}
