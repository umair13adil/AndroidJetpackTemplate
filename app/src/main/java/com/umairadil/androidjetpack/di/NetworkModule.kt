package com.umairadil.androidjetpack.di

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Timber.i(message)
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
    }

    @Singleton
    @Provides
    fun provideApiService(): RestService {
        return provideRetrofit().create(RestService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val client = provideClient()

        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }
}