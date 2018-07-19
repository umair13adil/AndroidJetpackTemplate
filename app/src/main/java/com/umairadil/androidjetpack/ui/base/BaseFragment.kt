package com.umairadil.androidjetpack.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import androidx.navigation.Navigation
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.ui.detail.DetailFragment
import com.umairadil.androidjetpack.ui.movies.items.MovieItem
import com.umairadil.androidjetpack.ui.movies.items.ProgressItem
import dagger.android.support.AndroidSupportInjection
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Publisher
import javax.inject.Inject


abstract class BaseFragment : Fragment(), IRxBusQueue,
        FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemSwipeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //List Adapter
    var adapter: FlexibleAdapter<*>? = null
    private var layoutManager: LinearLayoutManager? = null
    private var mItems: ArrayList<IFlexible<*>>? = null
    private var isExpanded = false
    var progressItem = ProgressItem()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    private val TAG = "BaseFragment"
    var fragmentActions: FragmentActions? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            adapter?.onRestoreInstanceState(savedInstanceState)
        }
    }

    //*********************//
    // List Adapter Start
    //*********************//

    /**
     * This will setup list adapter.
     */
    fun setUpAdapter(recyclerView: RecyclerView) {

        adapter = FlexibleAdapter(mItems, this, true)
        adapter?.setMode(SelectableAdapter.Mode.SINGLE)

        adapter?.setOnlyEntryAnimation(false)
                ?.setAnimationInterpolator(AccelerateDecelerateInterpolator())
                ?.setAnimationDelay(200L)
                ?.setAnimationOnForwardScrolling(true)
                ?.setAnimationOnReverseScrolling(false)

        layoutManager = SmoothScrollLinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()

        adapter?.setSwipeEnabled(true)
                ?.setNotifyChangeOfUnfilteredItems(true)
    }

    fun setEndlessScroll(scrollListener: FlexibleAdapter.EndlessScrollListener) {
        (adapter as? FlexibleAdapter<ProgressItem>)
                ?.setLoadingMoreAtStartUp(false)
                ?.setEndlessPageSize(20) //Endless is automatically disabled if newItems < 20 (Size of our response from server)
                ?.setEndlessScrollListener(scrollListener, progressItem)
    }

    //List Adapter's Callback
    override fun onItemSwipe(position: Int, direction: Int) {

        //Refresh tile
        adapter!!.notifyItemChanged(position)

        //Send Parent Swipe Callback
        if (direction == ItemTouchHelper.RIGHT) {
            /* if (listActions != null)
                 listActions?.swipedRight(true, position, 0)*/
        } else {
            /*if (listActions != null)
                listActions?.swipedLeft(true, position, 0)*/
        }
    }

    //List Adapter's Callback
    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {}

    //List Adapter's Callback
    override fun onItemClick(view: View?, position: Int): Boolean {

        val bundle = DetailFragment.bundleArgs(getListObject(position))
        Navigation.findNavController(view!!).navigate(R.id.action_movieFragment_to_detailFragment, bundle)

        return true
    }

    /***
     * This will clear all items from list.
     ***/
    fun clearListData() {
        try {
            if (isAdapterNotEmpty()) {
                mItems?.clear()
                adapter?.clear()
                adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * Will return true if items are added in adapter.
     ***/
    private fun isAdapterNotEmpty(): Boolean {
        return mItems != null && mItems?.isNotEmpty()!!
    }

    /***
     * This will clear filter from list adapter.
     ***/
    fun clearFilter() {
        if (isAdapterNotEmpty()) {
            adapter?.setFilter(null)
            adapter?.filterItems(mItems!! as List<Nothing>, 350)
        }
    }

    /***
     * This will set filter to list adapter.
     ***/
    fun setFilter(query: String) {
        if (isAdapterNotEmpty()) {
            if (adapter?.hasNewFilter(query)!!) {
                adapter?.setFilter(query)
                adapter?.filterItems(mItems!! as List<Nothing>, 350)
            } else {
                clearFilter()
            }
        }
    }

    /***
     * This will populate list adapter with data keeping first item as selected.
     ***/
    fun showOrHideList(recyclerView: RecyclerView, emptyView: View, movieList: List<Movie>) {

        if (mItems == null)
            mItems = arrayListOf<IFlexible<*>>()

        try {

            for (movie in movieList) {

                //Parent Item is an expandable with Level=0
                val movieItem = MovieItem(movie)

                //Add Item to List
                mItems?.add(movieItem)
            }

            adapter?.updateDataSet(mItems!! as List<Nothing>)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Show or Hide list
        if (!isAdapterNotEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }

    /**
     * This will return parent task item from list.
     */
    fun getListObject(position: Int): Movie? {
        val item = adapter?.getItem(position) as? MovieItem
        return item?.movie
    }


    //*********************//
    // List Adapter End
    //*********************//

    protected fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean) {
        if (fragmentActions != null) {
            fragmentActions!!.startFragment(fragment, clearBackstack, replace, forceStart)
        }
    }

    protected fun finish() {
        if (fragmentActions != null) {
            fragmentActions!!.finishFragment()
        }
    }

    interface FragmentActions {

        fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean)

        fun finishFragment()
    }

    override fun onResume() {
        super.onResume()
        mResumedProcessor.onNext(true)
    }

    override fun onPause() {
        mResumedProcessor.onNext(false)
        super.onPause()
    }

    override fun onDestroy() {
        RxDisposableManager.unsubscribe(this)
        super.onDestroy()
    }

    // --------------
    // Interface RxBus
    // --------------

    override fun isBusResumed(): Boolean {
        return mResumedProcessor.value!!
    }

    override fun getResumeObservable(): Publisher<Boolean> {
        return mResumedProcessor
    }

    // --------------
    // Commons
    // --------------

    fun showLoading(progressBar: ProgressBar?, emptyView: View?) {
        progressBar?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        progressBar?.isIndeterminate = true
    }

    fun hideLoading(progressBar: ProgressBar?, recyclerView: RecyclerView?) {

        //Show layout with animation
        recyclerView?.animate()
                ?.alpha(1f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(null)

        //Hide Progress Layout
        progressBar?.animate()
                ?.translationY(progressBar.height.toFloat())
                ?.alpha(0.0f)
                ?.setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBar.visibility = View.GONE
                    }
                })
    }
}
