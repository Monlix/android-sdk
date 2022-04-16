package com.monlixv2.service

import com.monlixv2.service.models.campaigns.Campaign
import com.monlixv2.service.models.offers.OfferResponse
import com.monlixv2.service.models.surveys.Survey
import com.monlixv2.service.models.transactions.TransactionResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiInterface {

    @GET("user/transactions")
    suspend fun getTransactions(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
        @Query("pageid") pageId: String,
        @Query("status") status: String,
    ): Response<TransactionResponse>

    @GET("surveys")
    suspend fun getSurveys(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<ArrayList<Survey>>

    @GET("offers")
    suspend fun getOffers(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<OfferResponse>

    @GET("campaigns")
    suspend fun getCampaigns(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
    ): Response<ArrayList<Campaign>>


    companion object {
        private var instance: ApiInterface? = null

        private var BASE_URL = "https://api.monlix.com/api/"
        private val okHttpClient = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        fun getInstance(): ApiInterface {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build()
                    .create(ApiInterface::class.java)
            return instance as ApiInterface
        }
    }
}
