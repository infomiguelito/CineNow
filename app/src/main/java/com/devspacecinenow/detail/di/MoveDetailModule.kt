package com.devspacecinenow.detail.di

import com.devspacecinenow.detail.data.MovieDetail
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class MovieDetailModule {
    @Provides
    fun providesDetailService(retrofit: Retrofit): MovieDetail {
        return retrofit.create(MovieDetail::class.java)
    }


}