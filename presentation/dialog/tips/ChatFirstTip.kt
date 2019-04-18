package org.zapomni.venturers.presentation.dialog.tips

import android.graphics.Point
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.tip_chat_limit.view.*
import org.jetbrains.anko.contentView
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.presentation.main.MainActivity

open class ChatFirstTip(private val activity: MainActivity,
                        private val limitOffset: Int,
                        private val offsetForSecondTip: Int,
                        private val cancelTips: () -> Unit) {

    protected val view = LayoutInflater.from(activity).inflate(R.layout.tip_chat_limit, null)

    protected val dialog = AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar)
            .setView(view)
            .setOnCancelListener { activity.hideKeyBoard(); ChatSecondTip(activity, offsetForSecondTip, cancelTips).show() }
            .create()

    fun show() {
        dialog.show()

        val point = Point()
        activity.windowManager.defaultDisplay.getSize(point)
        view.layoutParams = view.layoutParams.apply { height = point.y }

        val keyboardHeight = activity.contentView!!.rootView.height - activity.contentView!!.height

        with(view) {
            val offset = keyboardHeight + limitOffset + 42.dpToPx()
            llMain.layoutParams = (llMain.layoutParams as RelativeLayout.LayoutParams)
                    .apply { setMargins(16.dpToPx(), 0, 0, offset) }

            for (i in 0 until llMain.childCount) {
                (view as ViewGroup).getChildAt(i)?.setOnClickListener { dialog.cancel() }
            }
        }
    }
}