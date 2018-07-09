package com.umairadil.androidjetpack.data.repositories

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject public constructor(var movieApi: RestService) : MovieDataSource {

    //Synchronized mapping of objects in memory (In Memory Cache)
    private var cachedMovies = Collections.synchronizedMap(WeakHashMap<Int, List<Movie>>())

    //Empty observable for local cached list
    private fun getCachedMovies(page: Int): Observable<List<Movie>> {

        //Check if there is a list saved against provided key otherwise send empty list
        if (cachedMovies?.get(page) != null)
            return Observable.just(cachedMovies.get(page))
        else
            return Observable.just(arrayListOf())
    }

    /**
     *
     * This will return list of movies either cached or from server.
     *
     * @param page:Int number of page required for paging
     **/
    override fun getAllMovies(page: Int): Observable<List<Movie>> {

        //Check if cached movies are not empty
        if (cachedMovies?.get(page) == null || cachedMovies?.get(page)?.isEmpty()!!) {

            //Provide two observables of different sources
            return Observables.zip(movieApi.getMovies(page), getCachedMovies(page)) { server, local ->

                //Get Server Response as List
                val results = server.results as List<Movie>
                val movies = arrayListOf<Movie>()

                //Add all server response to list
                if (results.isNotEmpty())
                    movies.addAll(results)

                //Add all local response to list
                movies.addAll(local)

                //Save to in memory cache
                cachedMovies.put(page, movies)

                //Return zipped list
                movies
            }

        } else {
            return getCachedMovies(page)
                    .doOnNext {
                        Timber.i("Cached Movies Size: ${it.size}")
                    }
        }
    }

    override fun insertMovie(movie: Movie) {

    }
}