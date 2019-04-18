package org.zapomni.venturers.presentation.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.support.annotation.StringRes
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.longSnackbar
import org.zapomni.venturers.extensions.loge


abstract class BaseActivity<V : BaseView, P : BasePresenter<V>> : MvpActivity<V, P>(), BaseView {

    private var loadingAlert: DialogInterface? = null
    protected var boldTypeFace: Typeface? = null
    protected var mediumTypeFace: Typeface? = null

    override fun loading(loading: Boolean) {
        if (loading && loadingAlert == null) {
            loadingAlert = alert { title = "Loading..." }.show()
        } else {
            loadingAlert?.dismiss()
            loadingAlert = null
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyBoard()
    }

    override fun onError(throwable: Throwable?) {
        if (throwable?.message != null) {
            showMessage(throwable.message)
            loge(throwable.message ?: "", throwable)
        }
    }

    override fun showMessage(@StringRes message: Int) {
        showMessage(getString(message))
    }

    override fun showMessage(message: String?) {
        if (message != null) longSnackbar(findViewById(android.R.id.content), message)
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyBoard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(contentView?.windowToken, 0)
    }
}