package com.umairadil.androidjetpack.models.genre

import com.google.gson.annotations.SerializedName
import java.util.*

class GenreListResponse {

    @SerializedName("genres")
    var genres: ArrayList<Genre>? = null
}
