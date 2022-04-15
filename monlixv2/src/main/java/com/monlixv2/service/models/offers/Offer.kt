package com.monlixv2.service.models.offers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "offer")
data class Offer(
    @PrimaryKey @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,
    @SerializedName("payout")
    val payout: String,
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
)
