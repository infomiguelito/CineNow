package com.devspacecinenow

import android.app.Application
import androidx.room.Room
import com.devspacecinenow.common.data.remote.RetrofitClient
import com.devspacecinenow.common.data.local.CineNowDataBase
import com.devspacecinenow.list.data.MovieListRepository
import com.devspacecinenow.list.data.local.MovieListLocalDataSource
import com.devspacecinenow.list.data.remote.ListService
import com.devspacecinenow.list.data.remote.MovieListRemoteDataSource
import com.devspacecinenow.preferences.data.UserPreferencesRepository

class CineNowApplication: Application(){

    val repository by lazy {
        CineNowServiceLocator.getRepository(this)
    }
}
