package org.zapomni.venturers.presentation.base

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

abstract class BasePresenter<V : BaseView> : MvpPresenter<V> {

    private val disposables = CompositeDisposable()
    var disposable: Disposable = Disposables.disposed()
        set(value) {
            disposables.add(value)
        }

    var view: V? = null
        private set
    protected var loading: Boolean = false
        set(value) {
            field = value
            view?.loading(value)
        }

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun detachView(retainInstance: Boolean) {
        // nothing
    }

    override fun destroy() {
        disposables.clear()
    }

    protected fun <T> Observable<T>.execute(onNext: (T) -> Unit, onError: ((Throwable) -> Unit)? = null, onComplete: (() -> Unit)? = null, withLoading: Boolean = true) {
        disposable = subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (!loading && withLoading) loading = true }
                .doOnEach { loading = false }
                .subscribe(onNext::invoke, {
                    if (onError != null) onError.invoke(it)
                    else view?.onError(it)
                }, { onComplete?.invoke() })
    }

    protected fun <T> Single<T>.execute(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit)? = null, withLoading: Boolean = true) {
        disposable = subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (!loading && withLoading) loading = true }
                .doOnEvent { _, _ -> loading = false }
                .subscribe(onSuccess::invoke) {
                    if (onError != null) onError.invoke(it)
                    else view?.onError(it)
                }
    }

    protected fun Completable.execute(onSuccess: () -> Unit, onError: ((Throwable) -> Unit)? = null, withLoading: Boolean = true) {
        disposable = subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (!loading && withLoading) loading = true }
                .doOnEvent { loading = false }
                .subscribe(onSuccess::invoke) {
                    if (onError != null) onError.invoke(it)
                    else view?.onError(it)
                }
    }
}