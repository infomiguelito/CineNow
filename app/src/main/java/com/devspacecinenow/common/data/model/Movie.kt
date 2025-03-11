package com.devspacecinenow.common.data.model


data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val image: String,
    val category: String,
    val genres: List<String> = emptyList()
)
