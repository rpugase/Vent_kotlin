package org.zapomni.venturers.presentation.base

import android.support.annotation.StringRes
import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseView : MvpView {
    fun loading(loading: Boolean)
    fun onError(throwable: Throwable?)
    fun showMessage(message: String?)
    fun showMessage(@StringRes message: Int)
}