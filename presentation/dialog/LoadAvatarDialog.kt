package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.widget.Button
import kotlinx.android.synthetic.main.dialog_avatar.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class LoadAvatarDialog : BaseDialogFragment() {

    var onUnderstandingClickListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_avatar, root)

        with(mView) {
            findViewById<FloatingActionButton>(R.id.btnFinish).setOnClickListener { dismiss() }
            findViewById<Button>(R.id.btnUnderstand).setOnClickListener { dismiss(); onUnderstandingClickListener?.invoke() }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        tvLoadAvatar.typeface = boldTypeFace
        tvFriends.typeface = regularTypeFace
        tvDontWorry.typeface = regularTypeFace
    }
}