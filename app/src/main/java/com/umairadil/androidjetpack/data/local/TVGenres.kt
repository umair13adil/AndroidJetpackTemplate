package com.umairadil.androidjetpack.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TVGenres(@PrimaryKey var id: Int? = null, var name: String? = null) : RealmObject() {

    public fun TVGenres() {

    }
}