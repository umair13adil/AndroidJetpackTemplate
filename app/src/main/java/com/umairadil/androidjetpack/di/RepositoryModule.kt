package com.umairadil.androidjetpack.di

import com.umairadil.androidjetpack.data.local.RealmHelper
import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.data.repositories.main.MainRepository
import com.umairadil.androidjetpack.data.repositories.movie.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieDataSource(api: RestService, db: RealmHelper): MovieRepository {
        return MovieRepository(api,db)
    }

    @Provides
    @Singleton
    fun provideMainDataSource(api: RestService, db: RealmHelper): MainRepository {
        return MainRepository(api, db)
    }
}