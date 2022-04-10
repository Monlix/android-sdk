package com.monlixv2.service.models.campaigns

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class CampaignGoal(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("payout")
    val payout: String,
    ) : Parcelable
