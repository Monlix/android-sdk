package com.monlixv2.service.models.ads

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ad(
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
    @SerializedName("visited")
    val visited: Boolean,
) : Parcelable
