package com.umairadil.androidjetpack.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import dagger.android.AndroidInjection
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Publisher

abstract class BaseActivity : AppCompatActivity(), IRxBusQueue {

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mResumedProcessor.onNext(true)
    }

    override fun onPause() {
        mResumedProcessor.onNext(false)
        super.onPause()
    }

    override fun onDestroy() {
        RxDisposableManager.unsubscribe(this)
        super.onDestroy()
    }

    // --------------
    // Interface RxBus
    // --------------

    override fun isBusResumed(): Boolean {
        return mResumedProcessor.value!!
    }

    override fun getResumeObservable(): Publisher<Boolean> {
        return mResumedProcessor
    }

}