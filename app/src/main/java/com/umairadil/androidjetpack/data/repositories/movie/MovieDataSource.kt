package com.umairadil.androidjetpack.data.repositories.movie

import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable

interface MovieDataSource {

    fun clearCachedMovies()

    fun insertMovie(movie: Movie)

    fun getAllMovies(page: Int, year: Int, sortBy: String, genre: Int): Observable<List<Movie>>
}