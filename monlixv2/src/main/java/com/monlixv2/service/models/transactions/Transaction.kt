package com.monlixv2.service.models.transactions

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")
    val id: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("reward")
    val reward: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("goals")
    val goals: ArrayList<TransactionGoal>,
)
