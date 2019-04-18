package org.zapomni.venturers.presentation.chat.topic.background

import org.zapomni.venturers.domain.ChatInteractor
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.presentation.base.BasePresenter

class BackgroundTopicPresenter(private val chatInteractor: ChatInteractor) : BasePresenter<BackgroundTopicView>() {

    override fun attachView(view: BackgroundTopicView) {
        super.attachView(view)
        chatInteractor.loadBackgrounds()
                .execute(view::loadBackgrounds)
    }

    fun takePicture(backgroundModel: BackgroundModel?) {
        if (backgroundModel != null) {
            view?.sendBackgroundModel(backgroundModel)
        }
    }

    fun loadPictureFromStorage() {
        view?.openGallery()
    }
}