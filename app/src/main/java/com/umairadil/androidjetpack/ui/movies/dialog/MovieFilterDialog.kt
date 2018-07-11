package com.umairadil.androidjetpack.ui.movies.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import com.umairadil.androidjetpack.R
import kotlinx.android.synthetic.main.dialog_movie_filter.view.*
import java.util.*


class MovieFilterDialog : DialogFragment() {

    internal var listener: OnFilterListener? = null

    private val TAG = "MovieFilterDialog"

    fun setListener(onFilterListener: OnFilterListener) {
        this.listener = onFilterListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)
                .setCancelable(false)

        val i = activity?.layoutInflater

        val view = i?.inflate(R.layout.dialog_movie_filter, null)

        setYearsSpinner(view)

        builder.setView(view)

        return builder.create()
    }

    interface OnFilterListener {
        fun onFilter(year: Int?, sortBy: String, genre: String)
    }

    private fun setYearsSpinner(view: View?) {
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1950..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, years)
        view?.spinner_year?.adapter = adapter
    }
}