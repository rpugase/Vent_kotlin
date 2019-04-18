package org.zapomni.venturers.presentation.chat.topic.main

import org.zapomni.venturers.R
import org.zapomni.venturers.domain.ChatInteractor
import org.zapomni.venturers.presentation.base.BasePresenter
import java.io.File

class TopicPresenter(private val chatInteractor: ChatInteractor) : BasePresenter<TopicView>() {

    private object State {
        const val TOPIC = 0
        const val POLL = 1
    }

    var pictureId: String? = null
    var pictureFile: File? = null
    var spinnerState = State.TOPIC
        set(value) {
            field = value
            when (value) {
                State.TOPIC -> view?.showTopic()
                State.POLL -> view?.showPoll()
            }
        }

    override fun attachView(view: TopicView) {
        super.attachView(view)
        chatInteractor.loadBackgrounds().execute({ view.showBackgroundModel(it.first()) })
        view.addAnswerOption()
        view.addAnswerOption()
        spinnerState = State.TOPIC
    }

    fun createTopic(title: String, topic: String) {
        when {
            title.isBlank() -> view?.titleError(R.string.error_empty_field)
            topic.isBlank() -> view?.descriptionError(R.string.error_empty_field)
            else -> chatInteractor.createAgenda(title, topic, pictureId, pictureFile)
                    .execute({ view?.goBack(it) })
        }
    }

    fun createPoll(title: String, answerOptions: List<String>) {
        answerOptions.forEachIndexed { index, answer ->
            if (answer.isBlank()) {
                view?.answerOptionsError(index, R.string.error_empty_field)
                return
            }
        }

        when {
            title.isBlank() -> view?.titleError(R.string.error_empty_field)
            else -> chatInteractor.createPoll(title, answerOptions, pictureId, pictureFile)
                    .execute({ view?.goBack(it) })
        }
    }

    fun setTitle(title: String) {
        when (spinnerState) {
            State.TOPIC -> view?.setTitleTopic(title)
            State.POLL -> view?.setTitlePoll(title)
        }
    }
}