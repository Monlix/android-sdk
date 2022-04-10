package com.monlixv2.service.models.offers

import com.google.gson.annotations.SerializedName
import com.monlixv2.service.models.ads.Ad
import com.monlixv2.service.models.surveys.Survey

data class OfferResponse(
    @SerializedName("surveys")
    val surveys: ArrayList<Survey>,
    @SerializedName("offers")
    val offers: ArrayList<Ad>,
    @SerializedName("ads")
    val ads: ArrayList<Ad>,
)
