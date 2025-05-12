package com.example.android_project.api

import com.example.android_project.data.CardResponse
import com.example.android_project.data.SingleCardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("cards")
    suspend fun getCards(@Query("pageSize") pageSize: Int = 20): CardResponse

    @GET("cards")
    suspend fun getCardsByName(@Query("q") query: String): CardResponse

    @GET("cards/{id}")
    suspend fun getCardById(@Path("id") id: String): SingleCardResponse
}