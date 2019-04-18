package org.zapomni.venturers.presentation.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_curator.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.makeCall
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class CuratorDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_CURATOR = "ARG_CURATOR"
        private const val ARG_SHOW_PHONE = "ARG_SHOW_PHONE"

        fun show(fragmentManager: FragmentManager?, curator: Curator, showPhoneNumber: Boolean): CuratorDialog {
            return CuratorDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CURATOR, curator)
                    putBoolean(ARG_SHOW_PHONE, showPhoneNumber)
                }
                show(fragmentManager, BonusDialog::class.java.name)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_curator, null)

        val curator = arguments?.getParcelable<Curator>(ARG_CURATOR)

        with(mView) {
            tvName.typeface = boldTypeFace
            tvRating.typeface = regularTypeFace
            tvAbout.typeface = regularTypeFace
            tvContactPhone.typeface = regularTypeFace
            tvPhoneNumber.typeface = boldTypeFace
            btnCall.typeface = boldTypeFace

            btnFinish.setOnClickListener { dismiss() }
            imgAvatar.loadImage(curator?.image)
            tvName.text = "${curator?.name} ${curator?.surname}"
            rbCurator.rating = curator!!.rating.toInt().toFloat()
            tvRating.text = "${curator.rating} / ${curator.likes}"
            tvAbout.text = curator.about

            if (curator.phone != null) {
                llPhoneNumber.visibility = if (arguments?.getBoolean(ARG_SHOW_PHONE) == true) View.VISIBLE else View.GONE
                tvPhoneNumber.text = curator.phone.toPrettyPhoneNumber()
                btnCall.setOnClickListener { activity?.makeCall(curator.phone) }
            }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }
}