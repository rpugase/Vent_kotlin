package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Advert(val headImage: String = "",
                  val title: String = "",
                  val text: String = "",
                  val phoneNumber: String? = null,
                  val facebookLink: String? = null) : Parcelable {

    fun isEmpty() = title.isEmpty() && text.isEmpty() && headImage.isEmpty()
}