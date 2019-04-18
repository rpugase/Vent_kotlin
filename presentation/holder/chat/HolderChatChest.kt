package org.zapomni.venturers.presentation.holder.chat

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chat_chest.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Chest
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.ChatPresenter

class HolderChatChest(rootView: ViewGroup, private val presenter: ChatPresenter) : BaseViewHolder<Message>(rootView, R.layout.item_chat_chest) {
    @SuppressLint("SetTextI18n")
    override fun bind(item: Message) {
        with(itemView) {
            val chest = item.chest

            tvChestClose.typeface = boldTypeFace
            tvChestOpen.typeface = boldTypeFace
            tvAboutChest.typeface = regularTypeFace
            btnOpenChest.typeface = boldTypeFace
            tvWinnerName.typeface = semiBoldTypeFace

            if (chest?.winner == null) {
                llChestClosed.visibility = View.VISIBLE
                btnOpenChest.visibility = View.VISIBLE
                llChestOpen.visibility = View.GONE

                when (chest?.type) {
                    Chest.Type.GOLD -> {
                        tvChestClose.text = context.getString(R.string.gold_chest)
                        imgChestClose.setImageResource(R.drawable.chest_close_gold)
                    }
                    Chest.Type.SIMPLE -> {
                        tvChestClose.text = context.getString(R.string.simple_chest)
                        imgChestClose.setImageResource(R.drawable.chest_close_simple)
                    }
                }
            } else {
                llChestOpen.visibility = View.VISIBLE
                llChestClosed.visibility = View.GONE
                btnOpenChest.visibility = View.GONE

                imgAvatar.loadImage(chest.winner.image)
                tvWinnerName.text = "${chest.winner.name}"

                val spannableAbout = SpannableString(context.getString(R.string.open_chest_and_get, chest.price))
                spannableAbout.setSpan(StyleSpan(Typeface.BOLD), 24, spannableAbout.length, 0)
                tvAboutWinner.text = spannableAbout

                when (chest.type) {
                    Chest.Type.GOLD -> {
                        imgChestOpen.setImageResource(R.drawable.chest_open_gold)
                    }
                    Chest.Type.SIMPLE -> {
                        imgChestOpen.setImageResource(R.drawable.chest_open_simple)
                    }
                }
            }

            btnOpenChest.setOnClickListener {
                it.isClickable = false
                presenter.openChest(adapterPosition, chest!!.id)
            }
        }
    }
}