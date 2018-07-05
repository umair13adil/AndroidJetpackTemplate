package com.umairadil.androidjetpack.ui.base

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
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.ui.items.MovieItem
import com.umairadil.androidjetpack.ui.items.SimilarItem
import dagger.android.support.AndroidSupportInjection
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Publisher
import javax.inject.Inject

abstract class BaseFragment : Fragment(), IRxBusQueue, FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnItemSwipeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //List Adapter
    private var adapter: FlexibleAdapter<*>? = null
    private var layoutManager: LinearLayoutManager? = null
    private var mItems: ArrayList<IFlexible<*>>? = null
    private var isExpanded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    private val TAG = BaseFragment::class.java.simpleName
    var fragmentActions: FragmentActions? = null

    init {
        this.retainInstance = true
    }

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
    private fun setUpAdapter(recyclerView: RecyclerView) {

        adapter = FlexibleAdapter(mItems, this, true)
        adapter?.setMode(SelectableAdapter.Mode.SINGLE)

        adapter?.setOnlyEntryAnimation(false)
                ?.setAnimationInterpolator(AccelerateDecelerateInterpolator())
                ?.setAnimationDelay(10L)
                ?.setAnimationOnForwardScrolling(false)
                ?.setAnimationOnReverseScrolling(false)

        layoutManager = SmoothScrollLinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter?.setSwipeEnabled(true)?.setAutoScrollOnExpand(false)?.setRecursiveCollapse(true)
    }

    //List Adapter's Callback
    override fun onItemSwipe(position: Int, direction: Int) {

        //Refresh tile
        adapter!!.notifyItemChanged(position)

        //Parent Swiped
        if (adapter!!.getItem(position)?.itemViewType == 0) {

            //Select and show task detail
            selectAndShowDetail(position)

            //Send Parent Swipe Callback
            if (direction == ItemTouchHelper.RIGHT) {
                /* if (listActions != null)
                     listActions?.swipedRight(true, position, 0)*/
            } else {
                /*if (listActions != null)
                    listActions?.swipedLeft(true, position, 0)*/
            }

        } else {

            //Select and show task detail
            selectAndShowDetail(position)

            //Send Child Swipe Callback
            if (direction == ItemTouchHelper.RIGHT) {
                /*if (listActions != null)
                    listActions?.swipedRight(false, jobViewModel.parentPos, position)*/
            } else {
                /*if (listActions != null)
                    listActions?.swipedLeft(false, jobViewModel.parentPos, position)*/
            }
        }
    }

    //List Adapter's Callback
    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {}

    //List Adapter's Callback
    override fun onItemClick(view: View?, position: Int): Boolean {

        //Parent Clicked
        if (adapter!!.getItem(position)?.itemViewType == 0) {
            if (adapter?.getItem(position) != null && adapter?.getItem(position) is MovieItem) {
                selectItem(position, true)
            }
        } else {
            //Child Clicked
            if (adapter?.getItem(position) != null && adapter?.getItem(position) is SimilarItem) {
                selectItem(position, false)
            }
        }

        return true
    }

    /***
     * This will clear all items from list.
     ***/
    fun clearListData() {
        try {
            if (mItems != null && mItems?.isNotEmpty()!!) {
                mItems?.clear()
                adapter?.clear()
                adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * This will clear all selected items on list.
     ***/
    private fun clearAllSelections(isParent: Boolean) {
        for (i in adapter?.currentItems!!.indices) {
            try {
                val item = adapter?.currentItems!![i]
                if (item != null && item is MovieItem && isParent) {
                    item.isSelected = false
                    adapter?.notifyItemChanged(i)
                }
                if (item != null && item is SimilarItem && !isParent) {
                    item.isSelected = false
                    adapter?.notifyItemChanged(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /***
     * This will populate list adapter with data keeping first item as selected.
     ***/
    fun showOrHideList(recyclerView: RecyclerView, emptyView: View, movieList: List<Movie>) {

        if (movieList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            return
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }

        mItems = arrayListOf<IFlexible<*>>()

        try {
            var isSelected = true

            for (movie in movieList) {

                //Parent Item is an expandable with Level=0
                val movieItem = MovieItem(movie)
                movieItem.isSelected = isSelected
                isSelected = false

                //If is batch
                /*if (!movie.isSingleTask) {

                    //Parent Item's child list item
                    for (subTask in movie.subTasksList) {
                        val subTaskItem = SubTaskItem(subTask)
                        subTaskItem.screenID = jobViewModel.screenID
                        movieItem.addSubItem(subTaskItem)
                    }
                }*/

                //Add Item to List
                mItems?.add(movieItem)
            }

            setUpAdapter(recyclerView)
            adapter?.notifyItemRangeInserted(0, mItems?.size!!)

            selectFirstItem()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * This will select first item on list.
     ***/
    private fun selectFirstItem() {
        if (mItems != null && mItems?.isNotEmpty()!!) {
            onItemClick(adapter?.recyclerView?.getChildAt(0), 0)
        }
    }

    /***
     * This select an item on list along with it's detail on detail fragment.
     ***/
    private fun selectAndShowDetail(position: Int) {

    }

    /***
     * This will remove an item from list after swiping.
     ***/
    fun swipeRemoveItem(position: Int, isParent: Boolean) {
        try {
            if (mItems != null && mItems?.isNotEmpty()!!) {

                adapter?.removeItem(position)
                adapter?.notifyDataSetChanged()

                if (isParent)
                    mItems?.removeAt(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * This will select parent tile.
     ***/
    private fun selectParentPos(position: Int, select: Boolean) {
        if (adapter?.getItem(position) != null && adapter?.getItem(position) is MovieItem) {
            val item = adapter?.getItem(position) as MovieItem
            item.isSelected = select
            adapter?.notifyItemChanged(position)
        }
    }

    /***
     * This will select child tile.
     ***/
    private fun selectChildPos(position: Int, select: Boolean) {
        if (adapter?.getItem(position) != null && adapter?.getItem(position) is SimilarItem) {
            val item1 = adapter?.getItem(position) as SimilarItem
            item1.isSelected = select
            adapter?.notifyItemChanged(position)
        }
    }

    /***
     * This will un-select previous and then select current item on click.
     ***/
    private fun selectItem(adapterPosition: Int, isParent: Boolean) {

        //Clear previous selections
        clearAllSelections(isParent)

        //If parent tile is changed
        if (isParent) {

            //Clear only child selections
            clearAllSelections(false)
        }

        //Un-select previous & select current item
        if (isParent) {
            selectParentPos(adapterPosition, false)
            selectParentPos(adapterPosition, true)
        } else {
            selectChildPos(adapterPosition, false)
            selectChildPos(adapterPosition, true)
        }

        //Select item
        adapter?.toggleSelection(adapterPosition)

        //Toggle expansion if item is expandable
        if (isParent) {

            //Only toggle if item has more than 1 child
            val offset = toggleIfBatch(adapterPosition)

            //Scroll to selected position
            scrollToPosition(adapterPosition, offset)

        } /*else {
            if (selectedPosition != adapterPosition)
                isExpanded = false
        }*/
    }

    /**
     * This will return parent task item from list.
     */
    fun getParentTask(position: Int): Movie? {
        val item = adapter?.getItem(position) as? MovieItem
        return item?.movie
    }

    /***
     * This will smoothly scroll to selected position with offset.
     ***/
    private fun scrollToPosition(position: Int, offset: Int) {

        adapter?.recyclerView?.post {
            if (offset > 0)
                adapter?.recyclerView?.smoothScrollToPosition(Math.abs(position - (offset + 1)))
            else
                adapter?.recyclerView?.smoothScrollToPosition(position)
        }
    }

    /***
     * This will expand/collapse an item if is parent and a batch.
     ***/
    private fun toggleIfBatch(position: Int): Int {
        try {
            val collapsedCount = collapseAll()

            if (isBatch(position)) {

                if (adapter?.isExpanded(position)!! || isExpanded) {
                    adapter!!.collapse(position)
                    isExpanded = false
                } else {

                    val value = adapter!!.expand(position)

                    if (value > 0)
                        isExpanded = true
                }
            }

            return collapsedCount

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    /***
     * This will Collapse all expanded items.
     ***/
    private fun collapseAll(): Int {

        if (isExpanded) {
            val value = adapter?.collapseAll() as Int

            if (value > 0)
                isExpanded = false

            return value
        }

        return 0
    }

    /***
     * This will return true if item selected is a batch item.
     ***/
    private fun isBatch(position: Int): Boolean {

        return false
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

}
