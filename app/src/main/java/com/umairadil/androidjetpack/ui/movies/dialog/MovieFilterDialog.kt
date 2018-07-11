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
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.utils.Preferences
import com.umairadil.androidjetpack.utils.Utils
import dagger.android.support.AndroidSupportInjection
import io.realm.Realm
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

            //Save Filter Options
            Preferences.getInstance().save(activity!!, Constants.PREF_GENRE, genre)
            Preferences.getInstance().save(activity!!, Constants.PREF_YEAR, year)

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

        //Get Saved filter value
        val saved = Preferences.getInstance().getInt(activity!!, Constants.PREF_YEAR, thisYear)
        val index = list.binarySearch(saved)

        view?.spinner_year?.setSelection(index)
        view?.spinner_year?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                year = saved
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                year = list.get(p2)
            }
        }
    }

    private fun setSortSpinner(view: View?) {
        val list = resources.getStringArray(R.array.array_sort_by)

        val sortByList = arrayListOf<String>()
        val seperatedList = arrayListOf<String>()

        for (item in list) {
            val separated = Utils.getInstance().getSeparatedValues(item, ":")
            sortByList.add(separated.get(0)) //Sort By Name
            seperatedList.add(separated.get(0)) //Sort By Key
        }

        //Get Saved filter value
        val saved = Preferences.getInstance().getString(activity!!, Constants.PREF_SORT)
        val index = seperatedList.binarySearch { String.CASE_INSENSITIVE_ORDER.compare(it, saved) }

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, sortByList)
        view?.spinner_sort_by?.adapter = adapter
        view?.spinner_sort_by?.setSelection(index)
        view?.spinner_sort_by?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                val separated = Utils.getInstance().getSeparatedValues(list.first(), ":")
                Preferences.getInstance().save(activity!!, Constants.PREF_SORT, separated.get(0))
                sortBy = separated.get(1)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val separated = Utils.getInstance().getSeparatedValues(list.get(p2), ":")
                Preferences.getInstance().save(activity!!, Constants.PREF_SORT, separated.get(0))
                sortBy = separated.get(1)
            }
        }
    }


    private fun setGenreSpinner(view: View?) {

        val list = Realm.getDefaultInstance().copyFromRealm(db.findAll(MovieGenre().javaClass)).sortedBy { it.id }

        if (list.isEmpty())
            return

        val names = ArrayList<String>()

        for (genre in list) {
            names.add(genre.name!!)
        }

        //Get Saved filter value
        val saved = Preferences.getInstance().getInt(activity!!, Constants.PREF_GENRE, genre)
        val index = list.binarySearch { String.CASE_INSENSITIVE_ORDER.compare(it.id.toString(), saved.toString()) }

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, names)
        view?.spinner_genre?.adapter = adapter
        view?.spinner_genre?.setSelection(index)
        view?.spinner_genre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                genre = saved
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                genre = list.get(p2).id!!
            }
        }
    }
}