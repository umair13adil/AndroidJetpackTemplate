package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModel
import com.umairadil.androidjetpack.data.repositories.movie.MovieRepository
import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private var movieRepository: MovieRepository) : ViewModel() {

    fun getMovies(page: Int, year: Int, sortBy: String): Observable<List<Movie>> {
        return movieRepository.getAllMovies(page, year, sortBy)
    }
}
