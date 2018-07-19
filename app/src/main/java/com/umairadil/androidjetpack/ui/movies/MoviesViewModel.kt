package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModel
import androidx.work.*
import com.umairadil.androidjetpack.data.repositories.movie.MovieRepository
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.models.movies.MovieListResponse
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.worker.AnalyzeMoviesWorker
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private var movieRepository: MovieRepository) : ViewModel() {

    //Selected Filter
    var defaultYear = Calendar.getInstance().get(Calendar.YEAR)
    var defaultSort = "popularity.desc" //Get Most popular movies by default
    var defaultGenre = 28 //Action
    var currentPage = 0
    var firstPage = 1

    fun getMovies(page: Int, year: Int, sortBy: String, genre: Int): Observable<List<Movie>> {
        return movieRepository.getAllMovies(page, year, sortBy, genre)
    }

    fun searchMovies(page: Int, query: String): Observable<MovieListResponse> {
        return movieRepository.searchMovies(page, query)
    }

    fun clearCachedMovies() {
        movieRepository.clearCachedMovies()
    }


    /**
     * This will call work manager to analyze input query string and suggest relevant movies to user.
     **/
    fun doWorkOnSearchQuery(query: String) {

        val myData: Data = mapOf(Constants.KEY_QUERY_DATA to query).toWorkData()

        val myConstraints = Constraints.Builder()
                //.setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

        val request = OneTimeWorkRequestBuilder<AnalyzeMoviesWorker>()
                .setConstraints(myConstraints)
                .setInputData(myData)
                .build()

        WorkManager.getInstance()?.enqueue(request)
    }

    fun getSuggestedMovies(year: Int, sortBy: String, genre: Int): Observable<List<Movie>> {
        return movieRepository.getSuggestedMovies(year, sortBy, genre)
    }
}
