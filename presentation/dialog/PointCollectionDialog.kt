package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_point_collection.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.TripStartPoint
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseDialogFragment


class PointCollectionDialog : BaseDialogFragment() {

    companion object {

        private val ARG_START_POINT = "ARG_START_POINT"

        fun show(fragmentManager: FragmentManager?, startPoint: TripStartPoint) = PointCollectionDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_START_POINT, startPoint)
                show(fragmentManager, PointCollectionDialog::class.java.simpleName)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_point_collection, null)
        setTypeFace()

        val startPoint = arguments?.getParcelable<TripStartPoint>(ARG_START_POINT)!!
        val latLng = startPoint.coordinates

        // prepare intent for map
        val gmmIntentUri = Uri.parse("geo:${latLng.latitude},${latLng.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        with(mView) {
            if (mapIntent.resolveActivity(context?.packageManager) != null) {
                tvOpenMap.visibility = View.VISIBLE
            }

            imgMap.loadImage("https://maps.googleapis.com/maps/api/staticmap?center=${latLng.latitude},${latLng.longitude}&zoom=16&size=640x480&key=${getString(R.string.google_maps_key)}")
            tvAboutPoint.text = startPoint.description
            tvAddress.text = startPoint.address

            btnFinish.setOnClickListener { dialog.dismiss() }
            btnUnderstand.setOnClickListener { dialog.dismiss() }
            tvOpenMap.setOnClickListener { startActivity(mapIntent) }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvCollectionPoint.typeface = boldTypeFace
            tvAboutPoint.typeface = regularTypeFace
            tvAddress.typeface = regularTypeFace
            btnUnderstand.typeface = boldTypeFace
            tvOpenMap.typeface = regularTypeFace
            tvAddressLabel.typeface = semiBoldTypeFace
        }
    }
}