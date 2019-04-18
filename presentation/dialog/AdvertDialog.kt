package org.zapomni.venturers.presentation.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.dialog_advert.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Advert
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.makeCall
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class AdvertDialog : BaseDialogFragment() {

    companion object {

        private val ARG_ADVERT = "ARG_ADVERT"

        fun show(fragmentManager: FragmentManager?, advert: Advert) = AdvertDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ADVERT, advert)
                show(fragmentManager)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_advert, null, false)

        val advert = arguments!!.getParcelable<Advert>(ARG_ADVERT)

        with(mView) {
            tvTitle.typeface = boldTypeFace
            tvAbout.typeface = regularTypeFace
            tvInfoByPhone.typeface = regularTypeFace
            tvPhoneNumber.typeface = boldTypeFace
            btnFacebook.typeface = boldTypeFace

            imgHead.loadImage(advert.headImage)
            tvTitle.text = advert.title
            tvAbout.text = advert.text
            btnFinish.setOnClickListener { dialog.dismiss() }

            if (advert.phoneNumber != null) {
                tvInfoByPhone.visibility = View.VISIBLE
                tvPhoneNumber.visibility = View.VISIBLE
                tvPhoneNumber.text = advert.phoneNumber
                tvPhoneNumber.setOnClickListener { activity?.makeCall(advert.phoneNumber) }
            }
            if (advert.facebookLink != null) {
                btnFacebook.visibility = View.VISIBLE
                btnFacebook.setOnClickListener {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(advert.facebookLink)))
                }
            }

            // resolve margins
            if (advert.phoneNumber == null && advert.facebookLink != null) {
                btnFacebook.layoutParams = (btnFacebook.layoutParams as LinearLayout.LayoutParams)
                        .apply { setMargins(0, 24.dpToPx(), 0, 24.dpToPx()) }
            } else if (advert.phoneNumber != null && advert.facebookLink == null) {
                llAdditional.setPadding(0, 18.dpToPx(), 0, 24.dpToPx())
            } else if (advert.phoneNumber != null && advert.facebookLink != null) {
                btnFacebook.layoutParams = (btnFacebook.layoutParams as LinearLayout.LayoutParams)
                        .apply { setMargins(0, 18.dpToPx(), 0, 24.dpToPx()) }
                llAdditional.setPadding(0, 18.dpToPx(), 0, 0)
            }
        }

        return AlertDialog.Builder(context!!)
                .setView(mView)
                .create()
    }
}