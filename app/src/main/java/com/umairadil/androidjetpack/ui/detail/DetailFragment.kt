package com.umairadil.androidjetpack.ui.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.ui.base.BaseFragment
import com.umairadil.androidjetpack.utils.Constants
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailViewModel
    var movie: Movie? = null

    companion object {

        private const val ARG_MOVIE = "movie"

        fun bundleArgs(movie: Movie?): Bundle {
            return Bundle().apply {
                this.putParcelable(ARG_MOVIE, movie)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        //Get parcelable movie object here
        movie = arguments?.getParcelable(ARG_MOVIE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent(movie)
    }

    private fun setContent(movie: Movie?) {

        //Set content in Collapsing Toolbar
        toolbar_layout?.title = movie?.title
        toolbar_layout?.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        toolbar_layout?.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        toolbar_layout?.setContentScrimColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(activity!!, android.R.color.transparent))
        toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(activity!!, android.R.color.transparent))


        Picasso.with(activity).load(Constants.BASE_URL_IMAGE + movie?.posterPath).into(img_header)

        //Set content in details section
        txt_title?.text = movie?.title
        txt_description?.text = movie?.overview
    }

}
