package com.monlixv2.service.models.campaigns

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

const val CAMPAIGN_tABLE_NAME = "campaign"
const val PLATFORM_ANDROID = 0
const val PLATFORM_ALL = 1


@Entity(tableName = CAMPAIGN_tABLE_NAME)
data class Campaign(
    @PrimaryKey @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int,
    @SerializedName("campaignId")
    val campaignId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("oss")
    val oss: ArrayList<String>,
    @SerializedName("payout")
    val payout: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("goals")
    val goals: List<CampaignGoal>,
    @SerializedName("categories")
    val categories: ArrayList<String>,
    @SerializedName("multipleTimes")
    val multipleTimes: Boolean,
    @SerializedName("featured")
    var featured: Boolean,
    @SerializedName("url")
    val url: String,
    @SerializedName("hasGoals")
    val hasGoals: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("cr")
    val cr: Double,
    var platform: Int,
)

class GoalsTypeConverter {
    @TypeConverter
    fun listToJson(value: List<CampaignGoal>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<CampaignGoal>::class.java).toList()
}
