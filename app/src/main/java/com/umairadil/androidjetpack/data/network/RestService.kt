package com.umairadil.androidjetpack.data.network

import com.umairadil.androidjetpack.models.genre.GenreListResponse
import com.umairadil.androidjetpack.models.movies.MovieListResponse
import com.umairadil.androidjetpack.models.tv.TVListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface RestService {

    //Get List of Movies
    @GET("discover/movie")
    fun getMovies(@Query("page") page: Int,
                  @Query("primary_release_year") release_year: Int,
                  @Query("sort_by") sort_by: String,
                  @Query("with_genres") genre: String,
                  @Query("api_key") api_key: String
    ): Observable<MovieListResponse>

    //Get List of TV Shows
    @GET("discover/tv")
    fun getTVShows(@Query("page") page: Int,
                   @Query("sort_by") sort_by: String,
                   @Query("api_key") api_key: String
    ): Observable<TVListResponse>

    //Get List of Movies Genre
    @GET("genre/movie/list")
    fun getMoviesGenre(@Query("api_key") api_key: String): Observable<GenreListResponse>

    //Get List of TV Genre
    @GET("genre/tv/list")
    fun getTVGenre(@Query("api_key") api_key: String): Observable<GenreListResponse>
}