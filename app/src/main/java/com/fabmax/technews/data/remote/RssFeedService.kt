package com.fabmax.technews.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface RssFeedService {
    @GET
    suspend fun fetchFeed(@Url url: String): String
}
