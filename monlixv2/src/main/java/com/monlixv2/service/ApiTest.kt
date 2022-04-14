package com.monlixv2.service

import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.transactions.TransactionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiTest {


    @GET("survey_test.php")
    suspend fun getSurveys(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<ArrayList<Survey>>

    @GET("offers_test.php")
    suspend fun getOffers(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<OfferResponse>

    @GET("campaigns_test.php")
    suspend fun getCampaigns(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<ArrayList<Campaign>>


    companion object {
        private var instance: ApiTest? = null

        private var BASE_URL = "https://portalniksic.me/"

        fun getInstance(): ApiTest {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(ApiTest::class.java)
            return instance as ApiTest
        }
    }
}
