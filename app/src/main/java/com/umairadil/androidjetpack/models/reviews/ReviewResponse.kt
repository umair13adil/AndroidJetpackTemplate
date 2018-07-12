package com.umairadil.androidjetpack.models.reviews

import com.google.gson.annotations.SerializedName

class ReviewResponse{

    @SerializedName("id")
    private val id: Int? = null

    @SerializedName("page")
    private val page: Int? = null

    @SerializedName("results")
    private val results: List<Review>? = null

    @SerializedName("total_pages")
    private val totalPages: Int? = null

    @SerializedName("total_results")
    private val totalResults: Int? = null
}