package org.zapomni.venturers.presentation.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.zapomni.venturers.R

abstract class BaseDialogFragment : DialogFragment() {

    protected val boldTypeFace by lazy { ResourcesCompat.getFont(context!!, R.font.gotha_pro_bold) }
    protected val mediumTypeFace by lazy { ResourcesCompat.getFont(context!!, R.font.gotha_pro_medium) }
    protected val regularTypeFace by lazy { ResourcesCompat.getFont(context!!, R.font.source_sans_pro_regular) }
    protected val semiBoldTypeFace by lazy { ResourcesCompat.getFont(context!!, R.font.source_sans_pro_semi_bold) }

    protected lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun setTypeFace() {}

    fun show(fragmentManager: FragmentManager?) {
        show(fragmentManager, this::class.java.name)
    }
}