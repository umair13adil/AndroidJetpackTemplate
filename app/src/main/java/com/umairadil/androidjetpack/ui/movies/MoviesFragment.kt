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
import com.umairadil.androidjetpack.models.search.SearchQuery
import com.umairadil.androidjetpack.ui.base.BaseFragment
import com.umairadil.androidjetpack.utils.Constants
import eu.davidea.flexibleadapter.FlexibleAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.movie_fragment.*
import timber.log.Timber

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

        //Attach fast scroller to list adapter
        adapter?.setFastScroller(fast_scroller)

        //Endless Scroll listener Callbacks
        val scrollListener = object : FlexibleAdapter.EndlessScrollListener {
            override fun noMoreLoad(newItemsSize: Int) {}

            override fun onLoadMore(lastPosition: Int, currentPage: Int) {

                Timber.i("Load page number: ${currentPage + 1}")

                //Get page of movies list
                getMovies(currentPage + 1)
            }
        }

        //Attach endless scroll listener
        setEndlessScroll(scrollListener)

        //Get first page of movies list
        getMovies(1)

        //Listen for search query
        RxBusBuilder.create(SearchQuery::class.java)
                .withQueuing(this)
                .withMode(RxBusMode.Main)
                .subscribe { data ->
                    filterData(data.query)
                }
    }

    private fun filterData(query: String) {

        if (query != Constants.CLEAR_SEARCH) {

            if (!TextUtils.isEmpty(query)) {
                setFilter(query)
            } else {
                clearFilter()
            }

        } else {
            Timber.i("getMovies")
            getMovies(1)
        }
    }

    private fun getMovies(page: Int) {

        //Do nothing if page is less than 1
        if (page < 1)
            return

        //Do nothing if filter is applied
        if (adapter?.hasFilter()!!) {
            Timber.i("hasFilter")
            return
        }

        //Show ProgressBar
        showLoading(progress_bar, empty_view)

        //This will call API or fetch from local repository
        viewModel.getMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = {

                            //Hide ProgressBar
                            hideLoading(progress_bar, recycler_view)

                            //Add data to list adapter
                            showOrHideList(recycler_view, empty_view, it)
                        },
                        onError = {
                            it.printStackTrace()

                        },
                        onComplete = { }
                )
    }
}
