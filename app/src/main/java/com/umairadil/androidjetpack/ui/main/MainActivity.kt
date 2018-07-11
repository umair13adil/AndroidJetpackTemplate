package com.umairadil.androidjetpack.ui.main

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.michaelflisar.rxbus2.RxBus
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.search.SearchQuery
import com.umairadil.androidjetpack.ui.base.BaseActivity
import com.umairadil.androidjetpack.ui.movies.dialog.MovieFilterDialog
import com.umairadil.androidjetpack.utils.Constants
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.setUpGenres()

        val navController = Navigation.findNavController(this, R.id.movies_fragment)

        // Set up ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        //Setup navigation view with navigation controller
        navigation_view.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawer_layout, Navigation.findNavController(this, R.id.movies_fragment))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val filterItem = menu.findItem(R.id.action_filter)

        searchItem.isVisible = true

        searchItem.setOnMenuItemClickListener {

            val searchView = searchItem.actionView.findViewById<SearchView>(R.id.action_search) as SearchView
            val searchManager: SearchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }

                    //Clear focus & close search
                    searchItem.collapseActionView()
                    searchView.clearFocus()

                    //Send 'CLEAR SEARCH' value to clear filter
                    RxBus.get().send(SearchQuery(Constants.CLEAR_SEARCH))

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {

                    //Send search query to subscribers
                    RxBus.get().send(SearchQuery(s))

                    return false
                }
            })
            true
        }

        filterItem.setOnMenuItemClickListener {
            val dialog: DialogFragment = MovieFilterDialog()
            dialog.show(supportFragmentManager, "filterDialog")
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.movies_fragment)) || super.onOptionsItemSelected(item)
    }

}
