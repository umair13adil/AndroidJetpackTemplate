package com.umairadil.androidjetpack.data.repositories.main

import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var api: RestService) : MainDataSource {

    override fun getMoviesGenre() {

        api.getMoviesGenre(Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = { }
                )
    }

    override fun getTVGenre() {

        api.getTVGenre(Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = { }
                )
    }

}