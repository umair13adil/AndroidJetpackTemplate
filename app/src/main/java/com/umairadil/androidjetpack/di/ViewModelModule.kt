package com.umairadil.androidjetpack.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.umairadil.androidjetpack.ui.detail.DetailViewModel
import com.umairadil.androidjetpack.ui.movies.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * All ViewModel classes that uses Dagger2 injection, must be declared here to support constructor injection,
 * otherwise app will give following exception on runtime access:
 * IllegalArgumentException: "unknown model call class"
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(factory: MyViewModelFactory): ViewModelProvider.Factory
}