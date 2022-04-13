package com.monlixv2.service.models.surveys

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Survey(
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
    @SerializedName("platforms")
    val platforms: ArrayList<String>?,
    @SerializedName("minPayout")
    val minPayout: Int?,
    @SerializedName("maxPayout")
    val maxPayout: Int?,
    @SerializedName("minDuration")
    val minDuration: String?,
    @SerializedName("maxDuration")
    val maxDuration: String?,
    @SerializedName("rank")
    val rank: Double?,
) : Parcelable
