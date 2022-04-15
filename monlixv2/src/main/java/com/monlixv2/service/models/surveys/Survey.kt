package com.monlixv2.service.models.surveys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

const val SURVEY_TABLE_NAME = "survey"

@Entity(tableName = SURVEY_TABLE_NAME)
data class Survey(
    @PrimaryKey @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String,
    @SerializedName("payout")
    val payout: String?,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("minPayout")
    val minPayout: Double?,
    @SerializedName("maxPayout")
    val maxPayout: Double?,
    @SerializedName("minDuration")
    val minDuration: String?,
    @SerializedName("maxDuration")
    val maxDuration: String?,
    @SerializedName("rank")
    val rank: Double?,
)

class PlatformsTypeConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun frmArrayList(list: ArrayList<String?>): String {
        return Gson().toJson(list)
    }

}
