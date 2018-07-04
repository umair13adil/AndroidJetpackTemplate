package com.umairadil.androidjetpack.ui.items

import android.animation.Animator
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.utils.Utils
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IExpandable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.ExpandableViewHolder
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieItem(val movie: Movie) : AbstractFlexibleItem<MovieItem.ParentViewHolder>(), IExpandable<MovieItem.ParentViewHolder, SimilarItem> {

    val TAG = MovieItem::class.java.simpleName
    private val color = R.color.colorPrimaryDark

    /* subItems list */
    var similarMovieItems: MutableList<SimilarItem>? = null

    /* Flags for FlexibleAdapter */
    private var mExpanded = false

    //Boolean flags
    var isSelected = false

    init {
        isDraggable = false
        isSwipeable = true
        isSelectable = true
        isExpanded = false
    }

    override fun isExpanded(): Boolean {
        return mExpanded
    }

    override fun setExpanded(expanded: Boolean) {
        mExpanded = expanded
    }

    override fun getExpansionLevel(): Int {
        return 0
    }

    override fun getSubItems(): List<SimilarItem>? {
        return similarMovieItems
    }

    fun hasSubItems(): Boolean {
        return similarMovieItems != null && similarMovieItems!!.size > 0
    }

    fun removeSubItem(taskItem: SimilarItem?): Boolean {
        return taskItem != null && similarMovieItems!!.remove(taskItem)
    }

    fun removeSubItem(position: Int): Boolean {
        if (similarMovieItems != null && position >= 0 && position < similarMovieItems!!.size) {
            similarMovieItems!!.removeAt(position)
            return true
        }
        return false
    }

    fun addSubItem(subTaskItem: SimilarItem) {
        if (similarMovieItems == null)
            similarMovieItems = mutableListOf<SimilarItem>()
        similarMovieItems!!.add(subTaskItem)
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     */
    override fun equals(o: Any?): Boolean {
        if (o is MovieItem) {
            val inItem = o as MovieItem?
            return this.movie.id == inItem!!.movie.id
        }
        return false
    }

    override fun hashCode(): Int {
        return movie.id.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_movie
    }

    override fun getItemViewType(): Int {
        return 0
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ParentViewHolder {
        return ParentViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: ParentViewHolder, position: Int, payloads: MutableList<Any>?) {
        holder.itemView.setActivated(isSelected)

        holder.jobName.setText("")

        //This will change UI backgrounds & colors upon selection
        if (hasSubItems())
            doSelectionChanges(holder)
    }

    private fun doSelectionChanges(holder: ParentViewHolder) {

        if (isSelected) {
            setSelection(holder)
        } else {
            clearSelection(holder)
        }
    }

    class ParentViewHolder(view: View, adapter: FlexibleAdapter<*>) : ExpandableViewHolder(view, adapter) {

        val jobName: AppCompatTextView
        val jobFloor: AppCompatTextView
        val packages: AppCompatTextView

        var fView: View
        var leftView: View
        var rightView: View
        var leftImage: ImageView
        var rightImage: ImageView

        var swiped = false

        init {
            this.jobName = view.txt_movie_title
            this.jobFloor = view.txt_genere
            this.packages = view.txt_description

            this.fView = view.front_view
            this.leftView = view.rear_left_view
            this.rightView = view.rear_right_view
            this.leftImage = view.left_image
            this.rightImage = view.right_image

            //Apply Fonts
            jobName.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)
            jobFloor.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)
            packages.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)

        }

        override fun scrollAnimators(animators: List<Animator>, position: Int, isForward: Boolean) {
            AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.recyclerView)
        }

        override fun onItemReleased(position: Int) {
            swiped = mActionState == ItemTouchHelper.ACTION_STATE_SWIPE
            super.onItemReleased(position)
        }

        override fun getActivationElevation(): Float {
            return 0f
        }

        override fun shouldActivateViewWhileSwiping(): Boolean {
            return true//default=false
        }

        override fun shouldAddSelectionInActionMode(): Boolean {
            return false//default=false
        }

        override fun getFrontView(): View {
            return fView
        }

        override fun getRearLeftView(): View {
            return leftView
        }

        override fun getRearRightView(): View {
            return rightView
        }
    }

    override fun toString(): String {
        return "MovieItem[" + super.toString() + "//SubItems" + similarMovieItems + "]"
    }

    private fun setSelection(holder: ParentViewHolder) {
        val colorContent = R.color.colorWhite
        val context = holder.contentView.context

        holder.frontView.setBackgroundColor(ContextCompat.getColor(context, color))
        holder.jobName.setTextColor(ContextCompat.getColor(context, colorContent))
        holder.jobFloor.setTextColor(ContextCompat.getColor(context, colorContent))
        holder.packages.setTextColor(ContextCompat.getColor(context, colorContent))
    }

    private fun clearSelection(holder: ParentViewHolder) {
        val context = holder.contentView.context

        holder.frontView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        holder.jobName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        holder.jobFloor.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        holder.packages.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
    }

}