package com.umairadil.androidjetpack.di

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.data.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieDataSource(api: RestService): MovieRepository {
        return MovieRepository(api)
    }
}