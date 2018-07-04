package com.umairadil.androidjetpack.models.movies

import com.google.gson.annotations.SerializedName
import java.util.*

public class Movie {

    @SerializedName("vote_count")
    var voteCount: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("video")
    var video: Boolean = false

    @SerializedName("vote_average")
    var voteAverage: Double = 0.toDouble()

    @SerializedName("title")
    var title: String? = null

    @SerializedName("popularity")
    var popularity: Double = 0.toDouble()

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    var genreIds: ArrayList<String>? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("adult")
    var adult: Boolean = false

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null
}
