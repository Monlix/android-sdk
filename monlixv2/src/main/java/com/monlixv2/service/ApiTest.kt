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
    @GET("test994.php")
    suspend fun getTransactions(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
        @Query("pageid") pageId: String,
        @Query("status") status: String,
    ): Response<TransactionResponse>

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
