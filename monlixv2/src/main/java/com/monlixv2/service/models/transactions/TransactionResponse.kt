package com.monlixv2.service.models.transactions

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("ptcClicks")
    val ptcClicks: Double,
    @SerializedName("ptcEarnings")
    val ptcEarnings: Double,
    @SerializedName("completedOffers")
    val completedOffers: Double,
    @SerializedName("transactions")
    val transactions: ArrayList<Transaction>,
)
