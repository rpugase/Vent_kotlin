package org.zapomni.venturers.presentation.base

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
//import com.crashlytics.android.Crashlytics
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import org.jetbrains.anko.longToast
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.loge
import org.zapomni.venturers.presentation.dialog.ProgressDialog


abstract class BaseFragment<V : BaseView, P : BasePresenter<V>> : MvpFragment<V, P>(), BaseView {

    private var loadingAlert: ProgressDialog? = null

    protected var boldTypeFace: Typeface? = null
        private set
    protected var mediumTypeFace: Typeface? = null
        private set
    protected var regularTypeFace: Typeface? = null
        private set
    protected var semiBoldTypeFace: Typeface? = null
        private set

    abstract fun provideLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        boldTypeFace = ResourcesCompat.getFont(context!!, R.font.gotha_pro_bold)
        mediumTypeFace = ResourcesCompat.getFont(context!!, R.font.gotha_pro_medium)
        regularTypeFace = ResourcesCompat.getFont(context!!, R.font.source_sans_pro_regular)
        semiBoldTypeFace = ResourcesCompat.getFont(context!!, R.font.source_sans_pro_semi_bold)
        return inflater.inflate(provideLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTypeFace()
    }

    override fun onPause() {
        loadingAlert?.dismiss()
        loadingAlert = null
        super.onPause()
    }

    override fun loading(loading: Boolean) {
        if (loading && loadingAlert == null) {
            loadingAlert = ProgressDialog()
            loadingAlert?.show(fragmentManager, ProgressDialog::class.java.name)
        } else {
            loadingAlert?.dismiss()
            loadingAlert = null
        }
    }

    override fun onError(throwable: Throwable?) {
        if (throwable?.message != null) {
            showMessage(throwable.message)
            loge(throwable.message ?: "", throwable)
//            Crashlytics.logException(throwable)
        }
    }

    override fun showMessage(@StringRes message: Int) {
        showMessage(getString(message))
    }

    override fun showMessage(message: String?) {
        if (message != null) context?.longToast(message)
    }

    open fun setTypeFace() {
        // Nothing
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyBoard() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun getStatusBarHeight(): Int {
        val rectangle = Rect()
        val window = activity!!.window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }
}