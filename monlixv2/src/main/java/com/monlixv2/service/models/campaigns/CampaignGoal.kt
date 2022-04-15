package com.monlixv2.service.models.campaigns

import com.google.gson.annotations.SerializedName


data class CampaignGoal(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("payout")
    val payout: String,
)
