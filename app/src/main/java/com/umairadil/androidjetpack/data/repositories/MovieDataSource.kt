package com.umairadil.androidjetpack.data.repositories

import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable

interface MovieDataSource {

    fun insertMovie(movie: Movie)

    fun getAllMovies(page: Int): Observable<List<Movie>>
}