package com.devspacecinenow.list

import com.devspacecinenow.common.data.local.MovieCategory
import com.devspacecinenow.common.data.model.Movie
import com.devspacecinenow.list.data.MovieListRepository
import com.devspacecinenow.list.data.local.MovieListLocalDataSource
import com.devspacecinenow.list.data.remote.MovieListRemoteDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class MovieListRepositoryTest {

    private val local: MovieListLocalDataSource = mock()
    private val remote: MovieListRemoteDataSource = mock()

    private val underTest by lazy {
        MovieListRepository(
            local = local,
            remote = remote
        )
    }

    @Test
    fun `Given no internet connection When getting now playing movies Then return local data`() {
        runTest {
            // Given
            val localList = listOf(
                Movie(
                    id = 1,
                    title = "title1",
                    overview = "overview1",
                    image = "image1",
                    category = MovieCategory.NowPlaying.name
                    )
            )
            whenever(remote.getNowPlaying()).thenReturn(Result.failure(UnknownHostException("No internet")))
            whenever(local.getNowPlayingMovies()).thenReturn(localList)

            //Then
            val result = underTest.getNowPlaying()

            //When
            val expected = Result.success(localList)
            assertEquals(expected, result)

        }
    }

}