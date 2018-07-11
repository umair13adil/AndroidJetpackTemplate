package com.umairadil.androidjetpack.ui.main

import android.arch.lifecycle.ViewModel
import com.umairadil.androidjetpack.data.repositories.main.MainRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private var mainRepository: MainRepository) : ViewModel() {

    fun setUpGenres() {
        mainRepository.getMoviesGenre()
        mainRepository.getTVGenre()
    }
}