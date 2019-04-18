package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PollAnswer(val pollId: String,
                      val answer: String,
                      var count: Int,
                      var percent: Int = 0,
                      var win: Boolean = false,
                      var max: Int = 0) : Parcelable {

    fun fillData(maxAnswer: Int, winner: PollAnswer?) {
        if (maxAnswer != 0) {
            max = maxAnswer
            percent = 100 * count / max
            win = winner?.answer == answer
        }
    }
}