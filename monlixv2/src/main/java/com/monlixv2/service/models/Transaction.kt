package com.monlix.service.models

data class Transaction(
    val id: String,
    val currency: String,
    val name: String,
    val reward: Double,
    val status: String,
    val time: String
)
