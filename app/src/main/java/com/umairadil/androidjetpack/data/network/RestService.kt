package com.umairadil.androidjetpack.data.network

import com.umairadil.androidjetpack.models.movies.MovieListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface RestService {

    @GET("movie?primary_release_year=2010&sort_by=vote_average.desc&api_key=e95c5c33d38a6be5f25c91d64c8d80e0")
    fun getMovies(@Query("page") page: Int): Observable<MovieListResponse>
}