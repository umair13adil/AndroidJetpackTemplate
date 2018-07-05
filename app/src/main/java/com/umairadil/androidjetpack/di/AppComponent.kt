package com.umairadil.androidjetpack.di

import com.umairadil.androidjetpack.App
import com.umairadil.androidjetpack.ui.base.BaseActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BindingModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        RepositoryModule::class
))
interface AppComponent {

    fun inject(baseActivity: BaseActivity)

    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}