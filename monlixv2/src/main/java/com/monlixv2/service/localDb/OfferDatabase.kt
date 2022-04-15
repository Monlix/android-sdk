package com.monlixv2.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.ads.AdDao
import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.campaigns.CampaignDao
import com.monlixv2.service.models.campaigns.GoalsTypeConverter
import com.monlixv2.service.models.offers.Offer
import com.monlixv2.service.models.offers.OfferDao
import com.monlixv2.service.models.surveys.PlatformsTypeConverter
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.surveys.SurveyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Offer::class, Ad::class, Survey::class, Campaign::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PlatformsTypeConverter::class, GoalsTypeConverter::class)
public abstract class OfferDatabase : RoomDatabase() {

    abstract fun offerDao(): OfferDao
    abstract fun adDao(): AdDao
    abstract fun surveyDao(): SurveyDao
    abstract fun campaignDao(): CampaignDao

    private class OfferDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.offerDao().deleteAll()
                    database.adDao().deleteAllAds()
                    database.surveyDao().deleteAllSurveys()
                    database.campaignDao().deleteAllCampaigns()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OfferDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): OfferDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OfferDatabase::class.java,
                    "offers_database"
                )
                    .addCallback(OfferDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


