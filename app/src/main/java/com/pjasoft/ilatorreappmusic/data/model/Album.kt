package com.pjasoft.ilatorreappmusic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val image: String = "",
    val description: String = "",
    val genre: String = "",
    val year: Int = 0
)
