package com.devspacecinenow.list

import com.devspacecinenow.common.data.local.MovieCategory
import com.devspacecinenow.common.data.model.Movie
import com.devspacecinenow.list.data.MovieListRepository
import com.devspacecinenow.list.data.remote.MovieListRemoteDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class MovieListRepositoryTest {

    private val local = FakeMovieListLocalDataSource()
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
            local.nowPlaying = localList


            //Then
            val result = underTest.getNowPlaying()

            //When
            val expected = Result.success(localList)
            assertEquals(expected, result)

        }
    }


    @Test
    fun `Given no internet connection and no local data When getting now playing movies Then return remote result`() {
        runTest {
            // Given
            val remoteResult = Result.failure<List<Movie>>(UnknownHostException("No internet"))
            whenever(remote.getNowPlaying()).thenReturn(remoteResult)
            local.nowPlaying = emptyList()

            //Then
            val result = underTest.getNowPlaying()

            //When
            val expected = remoteResult
            assertEquals(expected, result)

        }
    }

    @Test
    fun `Given remote success When getting now playing movies Then update local data`() {
        runTest {
            // Given
            val list = listOf(
                Movie(
                    id = 1,
                    title = "title1",
                    overview = "overview1",
                    image = "image1",
                    category = MovieCategory.NowPlaying.name
                )
            )
            val remoteResult = Result.success(list)
            whenever(remote.getNowPlaying()).thenReturn(remoteResult)
           local.nowPlaying = list

            //Then
            val result = underTest.getNowPlaying()

            //When
            val expected = Result.success(list)
            assertEquals(expected, result)
            assertEquals(local.updateItems, list)
        }
    }

}