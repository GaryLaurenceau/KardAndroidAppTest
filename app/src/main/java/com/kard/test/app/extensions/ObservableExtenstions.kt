package com.kard.test.app.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable

private class LifecycleDisposable(private val obj: Disposable, private val step: Lifecycle.Event): LifecycleObserver, Disposable by obj {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(owner: LifecycleOwner) {
        if (step == Lifecycle.Event.ON_PAUSE) dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner) {
        if (step == Lifecycle.Event.ON_STOP) dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        if (step == Lifecycle.Event.ON_DESTROY) dispose()
    }
}

fun Disposable.attachToPauseLifecycle(owner: LifecycleOwner): Disposable {
    owner.lifecycle.addObserver(LifecycleDisposable(this, Lifecycle.Event.ON_PAUSE))
    return this
}

fun Disposable.attachToStopLifecycle(owner: LifecycleOwner): Disposable {
    owner.lifecycle.addObserver(LifecycleDisposable(this, Lifecycle.Event.ON_STOP))
    return this
}

fun Disposable.attachToDestroyLifecycle(owner: LifecycleOwner): Disposable {
    owner.lifecycle.addObserver(LifecycleDisposable(this, Lifecycle.Event.ON_DESTROY))
    return this
}
