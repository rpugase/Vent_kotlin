package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.data.repository.FirebaseRepository
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.ChatPresenter
import org.zapomni.venturers.presentation.holder.chat.HolderChatChest
import org.zapomni.venturers.presentation.holder.chat.HolderChatDate
import org.zapomni.venturers.presentation.holder.chat.HolderChatMe
import org.zapomni.venturers.presentation.holder.chat.HolderChatUser

class ChatAdapter(private val presenter: ChatPresenter) : BaseAdapter<BaseViewHolder<Message>, Message>() {

    private val VT_ME = 0
    private val VT_ME_WITHOUT_AVA = 1
    private val VT_USER = 2
    private val VT_USER_WITHOUT_AVA = 3

    private val VT_DATE = 6
    private val VT_CHEST = 7

    var onLoadNewMessageEvent: ((lastMessage: Message) -> Unit)? = null

    override fun getHolder(rootView: ViewGroup, viewType: Int) = when (viewType) {
        VT_ME -> HolderChatMe(rootView, presenter)
//        VT_ME_WITHOUT_AVA -> HolderChatMeWithoutAvatar(rootView, presenter)
        VT_USER -> HolderChatUser(rootView, presenter)
//        VT_USER_WITHOUT_AVA -> HolderChatUserWithoutAvatar(rootView, presenter)
        VT_DATE -> HolderChatDate(rootView)
        VT_CHEST -> HolderChatChest(rootView, presenter)
        else -> throw IllegalStateException("No such view")
    }

    override fun getItemViewType(position: Int) = items[position].let {
        return@let when {
            it.chest != null -> VT_CHEST
            it.user.name == null -> VT_DATE
            it.me -> VT_ME
            !it.me -> VT_USER
//            it.me && it.showAvatar -> VT_ME
//            it.me && !it.showAvatar -> VT_ME_WITHOUT_AVA
//
//            !it.me && it.showAvatar -> VT_USER
//            !it.me && !it.showAvatar -> VT_USER_WITHOUT_AVA
            else -> throw IllegalStateException("Unknow state")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Message>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position == 0 && items.size >= FirebaseRepository.START_PAGINATION_VALUE) {
            onLoadNewMessageEvent?.invoke(items.first())
        }
    }

    fun changeItem(indexOf: Int, message: Message) {
        items[indexOf] = message
        notifyItemChanged(indexOf)
    }

    fun changeItem(message: Message) {
        val indexOf = items.indexOfFirst { it.id == message.id }
        if (indexOf != -1) {
            items[indexOf] = message
            notifyItemChanged(indexOf)
        }
    }

    fun removeItem(message: Message) {
        val indexOf = items.indexOfFirst { it.id == message.id }
        if (indexOf != -1) {
            items.removeAt(indexOf)
            notifyItemRemoved(indexOf)
        }
    }
}