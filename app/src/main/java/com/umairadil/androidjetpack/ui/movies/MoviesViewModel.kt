package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModel
import com.umairadil.androidjetpack.data.repositories.movie.MovieRepository
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.models.movies.MovieListResponse
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private var movieRepository: MovieRepository) : ViewModel() {

    //Selected Filter
    var defaultYear = Calendar.getInstance().get(Calendar.YEAR)
    var defaultSort = ""
    var defaultGenre = 18
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
}
