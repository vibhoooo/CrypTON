package com.example.crypton.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET(
        "query?function=LISTING_STATUS"
    )
    suspend fun getListings(
        @Query(
            "api_key"
        )
        apiKey: String = API_KEY
    ): ResponseBody
    companion object {
        const val API_KEY = "SU3GVCUF3RQHIB2M"
        const val BASE_URL = "https://www.alphavantage.co/"
    }
}