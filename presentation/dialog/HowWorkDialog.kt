package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_how_work.*
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class HowWorkDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_how_work, null)

        with(mView) {
            findViewById<FloatingActionButton>(R.id.btnFinish).setOnClickListener { dismiss() }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        tvHowWork.typeface = boldTypeFace
        tvBonusSystem.typeface = regularTypeFace
    }
}