package com.umairadil.androidjetpack.ui.items

import android.animation.Animator
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.utils.Utils
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IExpandable
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.ExpandableViewHolder
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieItem(val movie: Movie) : AbstractFlexibleItem<MovieItem.ParentViewHolder>(), IExpandable<MovieItem.ParentViewHolder, SimilarItem>, IFilterable<String> {

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

    override fun filter(constraint: String?): Boolean {
        return movie.originalTitle != null && movie.originalTitle?.toLowerCase()?.trim()?.contains(constraint!!)!! ||
                movie.overview != null && movie.overview?.toLowerCase()?.trim()?.contains(constraint!!)!!
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ParentViewHolder {
        return ParentViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: ParentViewHolder, position: Int, payloads: MutableList<Any>?) {
        holder.itemView.setActivated(isSelected)

        holder.title.setText(movie.originalTitle)
        holder.overview.setText(movie.overview)
        holder.popularity.setText(movie.popularity.toString())


        Picasso.with(holder.itemView.context).load(Constants.BASE_URL_IMAGE + movie.posterPath).into(holder.poster)

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

        val title: AppCompatTextView
        val overview: AppCompatTextView
        val popularity: AppCompatTextView
        val poster: AppCompatImageView

        var fView: View
        var leftView: View
        var rightView: View
        var leftImage: ImageView
        var rightImage: ImageView

        var swiped = false

        init {
            this.title = view.txt_movie_title
            this.overview = view.txt_description
            this.popularity = view.txt_popularity
            this.poster = view.img_movie_poster

            this.fView = view.front_view
            this.leftView = view.rear_left_view
            this.rightView = view.rear_right_view
            this.leftImage = view.left_image
            this.rightImage = view.right_image

            //Apply Fonts
            title.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)
            overview.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)
            popularity.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)

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
        holder.title.setTextColor(ContextCompat.getColor(context, colorContent))
        holder.overview.setTextColor(ContextCompat.getColor(context, colorContent))
        holder.popularity.setTextColor(ContextCompat.getColor(context, colorContent))
    }

    private fun clearSelection(holder: ParentViewHolder) {
        val context = holder.contentView.context

        holder.frontView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        holder.overview.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        holder.popularity.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
    }

}