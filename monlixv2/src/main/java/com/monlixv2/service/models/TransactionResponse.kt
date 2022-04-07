package com.monlixv2.service.models

import com.google.gson.annotations.SerializedName
import com.monlix.service.models.Transaction

data class TransactionResponse(
    @SerializedName("ptrClicks")
    val ptcClicks: Double,
    @SerializedName("ptcEarnings")
    val ptcEarnings: Double,
    @SerializedName("completedOffers")
    val completedOffers: Double,
    @SerializedName("transactions")
    val transactions: ArrayList<Transaction>,
)
