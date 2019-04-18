package org.zapomni.venturers.presentation.chat.meet.list

import org.zapomni.venturers.domain.ChatInteractor
import org.zapomni.venturers.presentation.base.BasePresenter

class MeetsPresenter(private val chatInteractor: ChatInteractor) : BasePresenter<MeetsView>() {
    fun doOnLike(id: String, isLike: Boolean) {
        chatInteractor.makeLike(id, isLike)
    }
}