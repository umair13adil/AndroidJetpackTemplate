package com.umairadil.androidjetpack.models.movies

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Movie() : RealmObject(), Parcelable {

    public fun Movie() {

    }

    @SerializedName("vote_count")
    var voteCount: Int = 0

    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("video")
    var video: Boolean = false

    @SerializedName("vote_average")
    var voteAverage: Double = 0.toDouble()

    @SerializedName("title")
    var title: String? = null

    @SerializedName("genre")
    var popularity: Double = 0.toDouble()

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    var genreIds: RealmList<String>? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("adult")
    var adult: Boolean = false

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    var genreNames: String? = null

    constructor(parcel: Parcel) : this() {
        voteCount = parcel.readInt()
        id = parcel.readInt()
        video = parcel.readByte() != 0.toByte()
        voteAverage = parcel.readDouble()
        title = parcel.readString()
        popularity = parcel.readDouble()
        posterPath = parcel.readString()
        originalLanguage = parcel.readString()
        originalTitle = parcel.readString()
        backdropPath = parcel.readString()
        adult = parcel.readByte() != 0.toByte()
        overview = parcel.readString()
        releaseDate = parcel.readString()
        genreNames = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(voteCount)
        parcel.writeInt(id)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeDouble(voteAverage)
        parcel.writeString(title)
        parcel.writeDouble(popularity)
        parcel.writeString(posterPath)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalTitle)
        parcel.writeString(backdropPath)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(genreNames)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
