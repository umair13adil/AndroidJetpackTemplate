package com.umairadil.androidjetpack.ui.items

import android.animation.Animator
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.LinearLayout
import com.umairadil.androidjetpack.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.utils.Utils
import kotlinx.android.synthetic.main.item_similar_movies.view.*

class SimilarItem(val movie: Movie) : AbstractFlexibleItem<SimilarItem.ChildViewHolder>() {

    private val color = R.color.colorPrimaryDark
    var isSelected = false

    init {
        isDraggable = false
        isSwipeable = true
        isSelectable = true
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     */
    override fun equals(o: Any?): Boolean {
        if (o is SimilarItem) {
            val inItem = o as SimilarItem?
            return this.movie.id == inItem!!.movie.id
        }
        return false
    }

    override fun hashCode(): Int {
        return movie.id.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_similar_movies
    }

    override fun getItemViewType(): Int {
        return 1
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ChildViewHolder {
        return ChildViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: ChildViewHolder, position: Int, payloads: MutableList<Any>?) {

        holder.itemView.setActivated(isSelected)

        doSelectionChanges(holder)
    }

    private fun doSelectionChanges(holder: ChildViewHolder) {

        if (isSelected) {
            setSelection(holder)
        } else {
            clearSelection(holder)
        }
    }


    class ChildViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        val recepient: AppCompatTextView
        val packages: AppCompatTextView
        val itemIndicator: LinearLayout

        var fView: View
        var leftView: View
        var rightView: View

        var swiped = false

        init {
            this.recepient = view.txt_recipient
            this.packages = view.txt_description
            this.itemIndicator = view.item_indicator

            this.fView = view.front_view
            this.leftView = view.rear_left_view
            this.rightView = view.rear_right_view

            //Apply Fonts
            recepient.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)
            packages.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)
        }

        override fun scrollAnimators(animators: List<Animator>, position: Int, isForward: Boolean) {
            AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.recyclerView)
        }

        override fun onClick(view: View?) {
            super.onClick(view)
        }

        override fun onItemReleased(position: Int) {
            swiped = mActionState == ItemTouchHelper.ACTION_STATE_SWIPE
            super.onItemReleased(position)
        }

        override fun toggleActivation() {
            super.toggleActivation()

        }

        override fun getActivationElevation(): Float {
            return 0f
        }

        override fun shouldActivateViewWhileSwiping(): Boolean {
            return true//default=false
        }

        override fun shouldAddSelectionInActionMode(): Boolean {
            return true//default=false
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
        return "SimilarItem[" + super.toString() + "]"
    }

    private fun setSelection(holder: ChildViewHolder) {
        val colorContent = R.color.colorWhite
        val context = holder.contentView.context

        holder.contentView.setBackgroundColor(ContextCompat.getColor(context, color))
        holder.recepient.setTextColor(ContextCompat.getColor(context, colorContent))
        holder.packages.setTextColor(ContextCompat.getColor(context, colorContent))
    }

    private fun clearSelection(holder: ChildViewHolder) {
        val context = holder.contentView.context

        holder.contentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        holder.recepient.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        holder.packages.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
    }
}