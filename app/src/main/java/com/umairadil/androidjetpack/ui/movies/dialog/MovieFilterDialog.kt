package com.umairadil.androidjetpack.ui.movies.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.michaelflisar.rxbus2.RxBus
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.data.local.MovieGenre
import com.umairadil.androidjetpack.data.local.RealmHelper
import com.umairadil.androidjetpack.models.rxbus.FilterOptions
import com.umairadil.androidjetpack.utils.Utils
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.dialog_movie_filter.view.*
import java.util.*
import javax.inject.Inject


class MovieFilterDialog : DialogFragment() {

    @Inject
    lateinit var db: RealmHelper

    //Filter Items
    var year: Int = 0
    var sortBy: String = ""
    var genre: Int = 18

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Dagger
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!).setCancelable(false)
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_movie_filter, null)

        //Set year options
        setYearsSpinner(view)

        //Set sortBy options
        setSortSpinner(view)

        //Set genre options
        setGenreSpinner(view)

        view?.btn_apply?.setOnClickListener {
            RxBus.get().withSendToDefaultBus().send(FilterOptions(year, sortBy, genre))
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    private fun setYearsSpinner(view: View?) {
        val list = ArrayList<Int>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1950..thisYear) {
            list.add(i)
        }
        val adapter = ArrayAdapter<Int>(activity, android.R.layout.simple_spinner_item, list)
        view?.spinner_year?.adapter = adapter
        view?.spinner_year?.setSelection(list.lastIndex)
        view?.spinner_year?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                year = thisYear
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                year = list.get(p2)
            }
        }
    }

    private fun setSortSpinner(view: View?) {
        val list = resources.getStringArray(R.array.array_sort_by)

        val sortByList = arrayListOf<String>()

        for (item in list) {
            val separated = Utils.getInstance().getSeparatedValues(item, ":")
            sortByList.add(separated.get(0))
        }

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, sortByList)
        view?.spinner_sort_by?.adapter = adapter
        view?.spinner_sort_by?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                sortBy = ""
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val separated = Utils.getInstance().getSeparatedValues(list.get(p2), ":")
                sortBy = separated.get(1)
            }
        }
    }


    private fun setGenreSpinner(view: View?) {

        val list = db.findAll(MovieGenre().javaClass)

        if (list.isEmpty())
            return

        val names = ArrayList<String>()

        for (genre in list) {
            names.add(genre.name!!)
        }

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, names)
        view?.spinner_genre?.adapter = adapter
        view?.spinner_genre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                genre = list.get(p2).id!!
            }
        }
    }
}