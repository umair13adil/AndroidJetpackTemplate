package com.umairadil.androidjetpack.models.movies

import com.google.gson.annotations.SerializedName
import java.util.*

class MovieListResponse {

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("total_results")
    var totalResults: Int = 0

    @SerializedName("total_pages")
    var totalPages: Int = 0

    var results: ArrayList<Movie>? = null
}
