package com.pjasoft.ilatorreappmusic.data.repository

import com.pjasoft.ilatorreappmusic.data.model.Album
import com.pjasoft.ilatorreappmusic.data.network.RetrofitClient

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

object MusicRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAlbums(): Result<List<Album>> {
        return try {
            val response = api.getAlbums()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun getAlbumById(id: String): Result<Album> {
        return try {
            val response = api.getAlbumById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }
}
