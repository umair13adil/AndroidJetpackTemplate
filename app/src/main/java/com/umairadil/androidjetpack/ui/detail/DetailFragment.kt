package com.umairadil.androidjetpack.ui.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.ui.base.BaseFragment

class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailViewModel

    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

}
