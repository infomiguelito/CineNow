package com.devspacecinenow

import android.app.Application
import androidx.room.Room
import com.devspacecinenow.common.data.local.CineNowDataBase
import com.devspacecinenow.common.data.remote.RetrofitClient
import com.devspacecinenow.list.data.MovieListRepository
import com.devspacecinenow.list.data.local.LocalDataSource
import com.devspacecinenow.list.data.local.MovieListLocalDataSource
import com.devspacecinenow.list.data.remote.ListService
import com.devspacecinenow.list.data.remote.MovieListRemoteDataSource
import com.devspacecinenow.list.data.remote.RemoteDataSource
import com.devspacecinenow.preferences.data.UserPreferencesRepository

object CineNowServiceLocator {

    fun getRepository(application: Application): MovieListRepository {
        val db = Room.databaseBuilder(
            application.applicationContext,
            CineNowDataBase::class.java, "database-cine-now"
        ).build()


        val listService = RetrofitClient.retrofitInstance.create(ListService::class.java)

        val localDataSource: LocalDataSource = MovieListLocalDataSource(db.getMovieDao())

        val remoteDataSource: RemoteDataSource = MovieListRemoteDataSource(listService)

        val userPreferencesRepository: UserPreferencesRepository = UserPreferencesRepository(application.applicationContext)

        return MovieListRepository(
            local = localDataSource,
            remote = remoteDataSource,
            userPreferencesRepository = userPreferencesRepository
        )

    }

}