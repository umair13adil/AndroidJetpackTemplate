package com.umairadil.androidjetpack.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MovieGenre(@PrimaryKey var id: Int? = null, var name: String? = null) : RealmObject() {

    public fun MovieGenre() {

    }
}