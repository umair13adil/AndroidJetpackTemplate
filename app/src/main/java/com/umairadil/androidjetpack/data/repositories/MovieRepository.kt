package com.umairadil.androidjetpack.data.repositories

import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable
import java.util.concurrent.Executor
import javax.inject.Inject

class MovieRepository @Inject constructor(var executor: Executor) : MovieDataSource {

    override fun insertMovie(posts: Movie) {

    }

    override fun getAllMovies(): Observable<List<Movie>> {

        return Observable.amb(listOf())
    }
}