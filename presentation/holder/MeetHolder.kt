package org.zapomni.venturers.presentation.holder

import android.annotation.SuppressLint
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_meet.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.formatWithSimpleDateMeet
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.meet.list.MeetsPresenter

class MeetHolder(rootView: ViewGroup, private val presenter: MeetsPresenter) : BaseViewHolder<Message>(rootView, R.layout.item_meet) {
    @SuppressLint("SetTextI18n")
    override fun bind(item: Message) {
        with(itemView) {
            setIsRecyclable(false)
            tvName.typeface = boldTypeFace
            tvPlace.typeface = regularTypeFace
            tvTime.typeface = regularTypeFace
            tvNameUser.typeface = semiBoldTypeFace
            tvPhone.typeface = regularTypeFace

            tvName.text = item.meet?.name
            tvPlace.text = context.getString(R.string.place_colon, item.meet?.place)
            tvTime.text = context.getString(R.string.time_colon, item.meet?.time?.formatWithSimpleDateMeet())
            imgAvatar.loadImage(item.user.image)
            tvNameUser.text = "${item.user.name} ${item.user.surname}"
            tvPhone.text = item.user.phoneNumber?.toPrettyPhoneNumber()

            cbLike.countLikes = item.likes
            cbLike.checked = item.likePressed
            cbLike.onCheckedChangeListener = { item.likePressed = !it; presenter.doOnLike(item.id, it) }
        }
    }
}