package com.pjasoft.ilatorreappmusic.data.model

data class AlbumsResponse(
    val data: List<Album> = emptyList(),
    val message: String = "",
    val status: Int = 0
)

data class AlbumResponse(
    val data: Album = Album(),
    val message: String = "",
    val status: Int = 0
)
