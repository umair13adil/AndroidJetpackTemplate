package com.umairadil.androidjetpack.ui.base

import android.support.v4.app.Fragment
import com.michaelflisar.rxbus2.interfaces.IRxBusQueue
import com.michaelflisar.rxbus2.rx.RxDisposableManager
import io.reactivex.processors.BehaviorProcessor
import org.reactivestreams.Publisher

abstract class BaseFragment : Fragment(), IRxBusQueue {

    private val mResumedProcessor = BehaviorProcessor.createDefault(false)

    private val TAG = BaseFragment::class.java.simpleName
    var fragmentActions: FragmentActions? = null

    init {
        this.retainInstance = true
    }

    protected fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean) {
        if (fragmentActions != null) {
            fragmentActions!!.startFragment(fragment, clearBackstack, replace, forceStart)
        }
    }

    protected fun finish() {
        if (fragmentActions != null) {
            fragmentActions!!.finishFragment()
        }
    }

    interface FragmentActions {

        fun startFragment(fragment: BaseFragment, clearBackstack: Boolean, replace: Boolean, forceStart: Boolean)

        fun finishFragment()
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
