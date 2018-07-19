package com.umairadil.androidjetpack.worker

import androidx.work.Worker
import com.umairadil.androidjetpack.data.local.RealmHelper
import com.umairadil.androidjetpack.di.NetworkModule
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList

class AnalyzeMoviesWorker : Worker() {

    val maxResults = 10
    var addedResults = 0

    override fun doWork(): Result {

        val queryValue = inputData.getString(Constants.KEY_QUERY_DATA, "")

        if (queryValue.isNullOrBlank())
            return Worker.Result.FAILURE

        RealmHelper().remove(Movie().javaClass)

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

                            val genreList = it.results?.first()?.genreIds

                            for (movie in it.results!!) {
                                findSimilar(movie.id, genreList)
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

    private fun findSimilar(movieId: Int, genres: RealmList<String>?) {
        NetworkModule().provideApiService().getSimilarMovies(movieId, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            for (movie in it.results!!) {

                                for (genreId in genres!!) {

                                    if (movie.genreIds?.contains(genreId)!!) {

                                        if (addedResults <= maxResults) {

                                            RealmHelper().add(movie)
                                            addedResults++

                                        } else {
                                            break
                                        }
                                    }

                                }
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