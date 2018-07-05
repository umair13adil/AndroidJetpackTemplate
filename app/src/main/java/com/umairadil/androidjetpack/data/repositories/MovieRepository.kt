package com.umairadil.androidjetpack.data.repositories

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.models.movies.MovieListResponse
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class MovieRepository @Inject public constructor(var movieApi: RestService) : MovieDataSource {

    var cachedMovies = emptyList<Movie>()
    var movieObservable: Observable<List<Movie>> = Observable.just(cachedMovies)

    override fun insertMovie(movie: Movie) {

    }

    override fun getAllMovies(page: Int): Observable<List<Movie>> {

        if (cachedMovies.isEmpty()) {

            return Observable.zip(movieApi.getMovies(page), movieObservable, object : Function2<MovieListResponse, List<Movie>, List<Movie>>, BiFunction<MovieListResponse, List<Movie>, List<Movie>> {
                override fun invoke(p1: MovieListResponse, p2: List<Movie>): List<Movie> {
                    return p1.results!!
                }

                override fun apply(t1: MovieListResponse, t2: List<Movie>): List<Movie> {
                    val results = t1.results

                    val movies = arrayListOf<Movie>()

                    if (results?.isNotEmpty()!!)
                        movies.addAll(results)

                    movies.addAll(t2)

                    return movies
                }

            })
        } else {
            return Observable.just(cachedMovies)
                    .mergeWith(movieObservable)
                    .doOnNext {
                        cachedMovies = it
                        Timber.i("Cached List: ${cachedMovies.size}")
                    }
        }
    }
}