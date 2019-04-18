package org.zapomni.venturers.presentation.dialog.tips

import android.app.Activity
import android.graphics.Point
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.tip_subject.view.*
import org.zapomni.venturers.R

open class ChatSecondTip(private val activity: Activity,
                         private val limitOffset: Int,
                         private val cancelTips: () -> Unit) {

    protected val view = LayoutInflater.from(activity).inflate(R.layout.tip_subject, null)

    protected val dialog = AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar)
            .setView(view)
            .setOnCancelListener { cancelTips.invoke() }
            .create()

    fun show() {
        dialog.show()

        val point = Point()
        activity.windowManager.defaultDisplay.getSize(point)
        view.layoutParams = view.layoutParams.apply { height = point.y }

        with(view) {
            llMain.post {
                llMain.layoutParams = (llMain.layoutParams as FrameLayout.LayoutParams)
                        .apply { setPadding(0, 0, 0, limitOffset + swTopic.height / 2) }
            }

            for (i in 0 until llMain.childCount) {
                (view as ViewGroup).getChildAt(i)?.setOnClickListener { dialog.cancel() }
            }
        }
    }
}