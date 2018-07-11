package com.umairadil.androidjetpack

import android.app.Activity
import android.app.Application
import android.content.Context
import com.umairadil.androidjetpack.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Realm.init(this)
        Realm.setDefaultConfiguration(realmConfig)

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    companion object {

        fun getContext(): Context {
            return this.getContext()
        }
    }

    val realmConfig: RealmConfiguration
        get() = RealmConfiguration.Builder()
                .name("MovieDB")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
}