package com.monlixv2.service.models.transactions

import com.google.gson.annotations.SerializedName


data class TransactionGoal(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("payout")
    val payout: String,
    @SerializedName("status")
    val status: String,
    )
