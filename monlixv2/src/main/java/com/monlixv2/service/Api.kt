package com.monlixv2.service

import com.monlixv2.service.models.TransactionResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type


interface ApiInterface {

    @GET("user/transactions")
    suspend fun getTransactions(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
        @Query("pageid") pageId: String,
        @Query("status") status: String,
    ): Response<TransactionResponse>

    @GET("user/transactions")
     fun getTransactions2(
        @Query("appid") appId: String,
        @Query("userid") userId: String,
        @Query("subid") subId: String,
        @Query("pageid") pageId: String,
        @Query("status") status: String,
    ): Call<TransactionResponse>

    companion object {
        private var instance : ApiInterface? = null

        private var BASE_URL = "https://api.monlix.com/api/"

        fun getInstance(): ApiInterface {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(ApiInterface::class.java)
            return instance as ApiInterface
        }
    }
}
