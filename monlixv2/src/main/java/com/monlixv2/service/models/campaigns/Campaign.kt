package com.monlixv2.service.models.campaigns

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Campaign(
    @SerializedName("id")
    val id: Int,
    @SerializedName("campaignId")
    val campaignId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("oss")
    val oss: ArrayList<String>,
    @SerializedName("payout")
    val payout: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("goals")
    val goals: ArrayList<CampaignGoal>,
    @SerializedName("categories")
    val categories: ArrayList<String>,
    @SerializedName("multipleTimes")
    val multipleTimes: Boolean,
    @SerializedName("featured")
    val featured: Boolean,
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
) : Parcelable
