package com.monlix.service.models

data class TransactionResponse(
    val ptcClicks: Int,
    val ptcEarnings: Int,
    val transactions: ArrayList<Transaction>,
)
