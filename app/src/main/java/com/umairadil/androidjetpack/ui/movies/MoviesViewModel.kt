package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModel
import com.umairadil.androidjetpack.data.repositories.movie.MovieRepository
import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private var movieRepository: MovieRepository) : ViewModel() {

    //Selected Filter
    var defaultYear = Calendar.getInstance().get(Calendar.YEAR)
    var defaultSort = ""
    var defaultGenre = 18
    var currentPage = 1

    fun getMovies(page: Int, year: Int, sortBy: String, genre: Int): Observable<List<Movie>> {
        return movieRepository.getAllMovies(page, year, sortBy, genre)
    }

    fun clearCachedMovies(){
        movieRepository.clearCachedMovies()
    }
}
