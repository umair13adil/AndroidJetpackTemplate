package com.umairadil.androidjetpack.worker

import androidx.work.Worker
import com.umairadil.androidjetpack.data.local.RealmHelper
import com.umairadil.androidjetpack.di.NetworkModule
import com.umairadil.androidjetpack.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AnalyzeMoviesWorker : Worker() {

    override fun doWork(): Result {

        val queryValue = inputData.getString(Constants.KEY_QUERY_DATA, "")

        if (queryValue.isNullOrBlank())
            return Worker.Result.FAILURE

        findMovieWithSearchedQuery(queryValue)

        return Worker.Result.SUCCESS
    }

    private fun findMovieWithSearchedQuery(queryValue: String?) {
        NetworkModule().provideApiService().searchMovies(1, queryValue!!, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            //Max number of results you want to find similar movies for
                            val max = 2
                            var count = 0

                            for (movie in it.results!!) {
                                findSimilar(movie.id)
                                count++

                                //Break loop if max count is reached
                                if (count == max)
                                    break
                            }
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = {

                        }
                )
    }

    private fun findSimilar(movieId: Int) {
        NetworkModule().provideApiService().getSimilarMovies(movieId, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            for (movie in it.results!!) {
                                RealmHelper().add(movie)
                            }
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = {

                        }
                )
    }
}