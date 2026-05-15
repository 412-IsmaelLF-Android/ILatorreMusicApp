package com.pjasoft.ilatorreappmusic.data.network

import com.pjasoft.ilatorreappmusic.data.model.Album
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApiService {

    @GET("api/albums")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Response<Album>
}
