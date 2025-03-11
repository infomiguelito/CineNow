package com.devspacecinenow.common.data.remote.model

import com.google.gson.annotations.SerializedName


data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val postPath: String,
    @SerializedName("genre_ids")
    val genre_ids: List<Int> = emptyList()
) {
    val posterFullPath: String
        get() = "https://image.tmdb.org/t/p/w300$postPath"

}

