package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModel
import com.umairadil.androidjetpack.data.repositories.MovieRepository
import com.umairadil.androidjetpack.models.movies.Movie
import io.reactivex.Observable
import javax.inject.Inject

class MoviesViewModel @Inject public constructor(var movieRepository: MovieRepository) : ViewModel() {

    fun getMovies(page: Int): Observable<List<Movie>> {
        return movieRepository.getAllMovies(page)
    }
}
