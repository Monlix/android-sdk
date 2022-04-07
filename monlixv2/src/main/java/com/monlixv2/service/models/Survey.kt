package com.monlix.service.models

data class Survey(
    val id: Int,
    val payout: String,
    val currency: String,
    val link: String,
    val name: String,
    val description: String,
    val logo: String,
    val provider: String
)
