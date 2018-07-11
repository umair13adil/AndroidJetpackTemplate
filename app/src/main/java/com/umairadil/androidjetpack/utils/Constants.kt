package com.umairadil.androidjetpack.utils

object Constants {

    //String base_url="https://api.themoviedb.org/3/discover/movie?primary_release_year=2010&sort_by=vote_average.desc&api_key=e95c5c33d38a6be5f25c91d64c8d80e0&page=1";

    var BASE_URL_API = "https://api.themoviedb.org/3/"
    var BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500/"
    val API_KEY = "e95c5c33d38a6be5f25c91d64c8d80e0"

    //Font Types
    val FONT_ROBOTO_BLACK = 0
    val FONT_ROBOTO_BLACK_ITALIC = 1
    val FONT_ROBOTO_BOLD_ITALIC = 3
    val FONT_ROBOTO_ITALIC = 4
    val FONT_ROBOTO_LIGHT = 5
    val FONT_ROBOTO_LIGHT_ITALIC = 6
    val FONT_ROBOTO_MEDIUM = 7
    val FONT_ROBOTO_MEDIUM_ITALIC = 8
    val FONT_ROBOTO_BOLD = 2
    val FONT_ROBOTO_REGULAR = 9
    val FONT_ROBOTO_THIN = 10
    val FONT_ROBOTO_THIN_ITALIC = 11
    val FONT_ROBOTO_CONDENSED_BOLD = 12
    val FONT_ROBOTO_CONDENSED_BOLD_ITALIC = 13
    val FONT_ROBOTO_CONDENSED_ITALIC = 14
    val FONT_ROBOTO_CONDENSED_LIGHT = 15
    val FONT_ROBOTO_CONDENSED_LIGHT_ITALIC = 16
    val FONT_ROBOTO_CONDENSED_REGULAR = 17

    //Search
    const val CLEAR_SEARCH = "%clear1%"

    //Filters
    const val PREF_YEAR = "pref_year"
    const val PREF_SORT = "pref_sort"
    const val PREF_GENRE = "pref_genre"
}