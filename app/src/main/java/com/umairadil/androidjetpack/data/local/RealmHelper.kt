package com.umairadil.androidjetpack.data.local

import io.realm.Realm
import io.realm.RealmObject


class RealmHelper {

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