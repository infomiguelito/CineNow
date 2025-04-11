package com.devspacecinenow.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devspacecinenow.common.data.local.CineNowDataBase
import com.devspacecinenow.common.data.local.MovieDao
import com.devspacecinenow.common.data.remote.RetrofitClient
import com.devspacecinenow.list.data.remote.ListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CineNowModule {

    @Provides
    fun providesCineNowDataBase(application: Application): CineNowDataBase {
       return Room.databaseBuilder(
            application.applicationContext,
            CineNowDataBase::class.java, "database-cine-now"
        ).build()
    }

    @Provides
    fun provideMovieDao(roomDatabase: CineNowDataBase): MovieDao {
        return roomDatabase.getMovieDao()
    }

    @Provides
    fun providesRetrofit(): Retrofit {
        return RetrofitClient.retrofitInstance
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @DispatcherIO
    fun providesDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}