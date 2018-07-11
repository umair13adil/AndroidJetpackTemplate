package com.umairadil.androidjetpack.data.repositories.main

import com.umairadil.androidjetpack.data.local.MovieGenre
import com.umairadil.androidjetpack.data.local.RealmHelper
import com.umairadil.androidjetpack.data.local.TVGenres
import com.umairadil.androidjetpack.data.network.RestService
import com.umairadil.androidjetpack.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private var api: RestService, private var db: RealmHelper) : MainDataSource {

    override fun getMoviesGenre() {

        //Do nothing if genre are added
        if (db.findAll(MovieGenre().javaClass).isNotEmpty())
            return

        api.getMoviesGenre(Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            val results = it.genres

                            for (genre in results!!) {
                                db.add(MovieGenre(genre.id, genre.name))
                            }
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = { }
                )
    }

    override fun getTVGenre() {

        //Do nothing if genre are added
        if (db.findAll(TVGenres().javaClass).isNotEmpty())
            return

        api.getTVGenre(Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            val results = it.genres

                            for (genre in results!!) {
                                db.add(TVGenres(genre.id, genre.name))
                            }
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = { }
                )
    }

}