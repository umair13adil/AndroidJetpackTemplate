package com.umairadil.androidjetpack.di

import android.content.Context
import com.michaelflisar.rxbus2.RxBus
import com.michaelflisar.rxbus2.RxBusSenderBuilder
import com.umairadil.androidjetpack.App
import com.umairadil.androidjetpack.data.local.RealmHelper
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [AndroidInjectionModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(): Context = App.getContext()

    @Provides
    @Singleton
    fun provideRxBus(): RxBusSenderBuilder {
        return RxBus.get()
    }

    @Provides
    @Singleton
    fun provideRealm(): RealmHelper {
        return RealmHelper()
    }
}