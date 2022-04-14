package com.monlixv2.service.models.transactions

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("ptcClicks")
    val ptcClicks: Int,
    @SerializedName("ptcEarnings")
    val ptcEarnings: Double,
    @SerializedName("completedOffers")
    val completedOffers: Int,
    @SerializedName("transactions")
    val transactions: ArrayList<Transaction>,
)
