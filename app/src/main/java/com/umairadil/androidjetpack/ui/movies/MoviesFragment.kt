package com.umairadil.androidjetpack.ui.movies

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.michaelflisar.rxbus2.RxBusBuilder
import com.michaelflisar.rxbus2.rx.RxBusMode
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.rxbus.FilterOptions
import com.umairadil.androidjetpack.models.search.SearchQuery
import com.umairadil.androidjetpack.ui.base.BaseFragment
import com.umairadil.androidjetpack.utils.Constants
import eu.davidea.flexibleadapter.FlexibleAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.movie_fragment.*

class MoviesFragment : BaseFragment() {

    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up list adapter
        setUpAdapter(recycler_view)

        //Endless Scroll listener Callbacks
        val scrollListener = object : FlexibleAdapter.EndlessScrollListener {
            override fun noMoreLoad(newItemsSize: Int) {}

            override fun onLoadMore(lastPosition: Int, currentPage: Int) {

                viewModel.currentPage = currentPage + 1

                //Get page of movies list
                getMovies(viewModel.currentPage, viewModel.defaultYear, viewModel.defaultSort, viewModel.defaultGenre)
            }
        }

        //Attach endless scroll listener
        setEndlessScroll(scrollListener)

        //Get first page of movies list
        getMovies(viewModel.firstPage, viewModel.defaultYear, viewModel.defaultSort, viewModel.defaultGenre)

        //Listen for search query
        RxBusBuilder.create(SearchQuery::class.java)
                .withQueuing(this)
                .withMode(RxBusMode.Main)
                .subscribe { data ->
                    filterData(data.query, data.action)
                }

        //Listen for filter query
        RxBusBuilder.create(FilterOptions::class.java)
                .withQueuing(this)
                .withMode(RxBusMode.Main)
                .subscribe { data ->
                    viewModel.defaultYear = data.year
                    viewModel.defaultSort = data.sortBy
                    viewModel.defaultGenre = data.genre

                    //Clear previous list data
                    clearListData()

                    //Clear all cached movies
                    viewModel.clearCachedMovies()

                    //Get page of movies list
                    getMovies(viewModel.firstPage, viewModel.defaultYear, viewModel.defaultSort, viewModel.defaultGenre)
                }
    }

    private fun filterData(query: String, action: String) {

        when (action) {

            Constants.CLEAR_SEARCH -> {
                getMovies(viewModel.currentPage, viewModel.defaultYear, viewModel.defaultSort, viewModel.defaultGenre)
            }

            Constants.SEARCH_QUERY_SUBMITTED -> {
                searchMovies(viewModel.firstPage, query)
            }

            Constants.SEARCH_AND_FILTER -> {
                if (!TextUtils.isEmpty(query)) {
                    setFilter(query)
                } else {
                    clearFilter()
                }
            }
        }
    }

    private fun getMovies(page: Int, year: Int, sortBy: String, genre: Int) {

        //Do nothing if page is less than 1
        if (page < 1)
            return

        //Do nothing if filter is applied
        if (adapter?.hasFilter()!!) {
            return
        }

        //This will call API or fetch from local repository
        viewModel.getMovies(page, year, sortBy, genre)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    //Show ProgressBar
                    showLoading(progress_bar, empty_view)
                }
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            //Add data to list adapter
                            showOrHideList(recycler_view, empty_view, it)
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = {
                            //Hide ProgressBar
                            hideLoading(progress_bar, recycler_view)
                        }
                )
    }

    private fun searchMovies(page: Int, query: String) {

        //This will call API to search for movies
        viewModel.searchMovies(page, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    //Show ProgressBar
                    showLoading(progress_bar, empty_view)
                }
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            //Save query keyword for analyzing user data
                            viewModel.doWorkOnSearchQuery(query)

                            //Clear previous list data
                            clearListData()

                            //Add data to list adapter
                            showOrHideList(recycler_view, empty_view, it.results!!)
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = {
                            //Hide ProgressBar
                            hideLoading(progress_bar, recycler_view)
                        }
                )
    }
}
