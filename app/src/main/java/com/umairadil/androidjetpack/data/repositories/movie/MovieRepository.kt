package com.umairadil.androidjetpack.data.repositories.movie

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(private var api: RestService) : MovieDataSource {

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
    override fun getAllMovies(page: Int, year: Int, sortBy: String, genre: Int): Observable<List<Movie>> {

        //If there are no cached movies, fetch movies from server
        if (cachedMovies?.get(page) == null || cachedMovies?.isEmpty()!!) {
            return getServerMovies(page, year, sortBy, genre.toString())
        } else {
            if (cachedMovies?.containsKey(page)!! && cachedMovies?.isNotEmpty()!!) {
                return getLocalMovies(page)
            } else {
                return getServerMovies(page, year, sortBy, genre.toString())
            }
        }
    }

    private fun getServerMovies(page: Int, year: Int, sortBy: String, genre: String): Observable<List<Movie>> {
        //Provide two observables of different sources
        return Observables.zip(api.getMovies(page, year, sortBy, genre, Constants.API_KEY), getCachedMovies(page)) { server, local ->

            //Get Server Response as List
            val results = server.results as List<Movie>
            val movies = arrayListOf<Movie>()

            //Add all server response to list
            if (results.isNotEmpty())
                movies.addAll(results)

            //Add all local response to list
            movies.addAll(local)

            //Save to in memory cache
            cachedMovies?.put(page, movies)

            //Return zipped list
            movies
        }
    }

    private fun getLocalMovies(page: Int): Observable<List<Movie>> {
        return getCachedMovies(page)
                .doOnNext {
                    Timber.i("Cached Movies Size: ${it.size} Page: $page")
                }
    }

    override fun insertMovie(movie: Movie) {

    }

    override fun clearCachedMovies() {
        cachedMovies?.clear()
    }
}