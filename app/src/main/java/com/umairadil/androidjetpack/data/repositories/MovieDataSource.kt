package com.umairadil.androidjetpack.data.repositories

import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable

interface MovieDataSource {

    fun insertMovie(posts: Movie)

    fun getAllMovies(): Observable<List<Movie>>
}