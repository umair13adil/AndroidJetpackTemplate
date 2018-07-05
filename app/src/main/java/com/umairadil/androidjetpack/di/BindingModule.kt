package com.umairadil.androidjetpack.di

import com.umairadil.androidjetpack.ui.base.BaseActivity
import com.umairadil.androidjetpack.ui.base.BaseFragment
import com.umairadil.androidjetpack.ui.main.MainActivity
import com.umairadil.androidjetpack.ui.movies.MoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * All components (Activity, Fragment, Services) that have been injected, must be declared here,
 * otherwise app will give exception during run-time.
 *
 * App can give following exceptions during run-time:
 * 1. UninitializedPropertyAccessException: lateinit property has not been initialized
 * 2. IllegalArgumentException: No injector factory bound
 */
@Module
internal abstract class BindingModule {

    /****************************
     * Activities
     * **************************/

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun baseActivity(): BaseActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    /****************************
     ** Fragments
     ****************************/

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun baseFragment(): BaseFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun moviesFragment(): MoviesFragment


    /****************************
     ** Dialogs
     ****************************/


    /****************************
     ** Services
     ****************************/
}