package com.monlixv2.service.models.surveys

import com.google.gson.annotations.SerializedName

data class Survey(
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
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("platforms")
    val platforms: ArrayList<String>
)
