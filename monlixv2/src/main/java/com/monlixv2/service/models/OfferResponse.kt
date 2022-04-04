package com.monlix.service.models

data class OfferResponse(
    val surveys: ArrayList<Offer>,
    val offers: ArrayList<Offer>,
    val ads: ArrayList<Offer>,
)
