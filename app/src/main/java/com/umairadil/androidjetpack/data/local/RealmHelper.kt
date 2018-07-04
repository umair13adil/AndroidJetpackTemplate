package com.umairadil.androidjetpack.data.local

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject
import javax.inject.Inject


class RealmHelper {

    @Inject
    lateinit var context: Context

    private fun getRealmInstance(): Realm {
        return Realm.getDefaultInstance()
    }

    fun <T : RealmObject> add(model: T): T {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(model)
        }
        return model
    }

    fun <T : RealmObject> update(model: T): T {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(model)
        }
        return model
    }

    fun <T : RealmObject> remove(model: T): T {
        val realm = getRealmInstance()
        realm.executeTransaction {
            it.deleteAll()
        }
        return model
    }

    fun <T : RealmObject> findAll(clazz: Class<T>): List<T> {
        return getRealmInstance().where(clazz).findAll()
    }

}