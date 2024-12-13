package com.devspacecinenow
@kotlinx.serialization.Serializable

data class MovieResponse(
    val result: List<MovieDto>
)
