package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class ProgressDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return AlertDialog.Builder(context)
                .setView(LayoutInflater.from(context).inflate(R.layout.dialog_progress, null))
                .create()
    }

    override fun setTypeFace() {}
}