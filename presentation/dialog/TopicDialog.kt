package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_topic.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Agenda
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class TopicDialog : BaseDialogFragment() {

    companion object {
        private val ARG_AGENDA = "ARG_AGENDA"

        fun newInstance(agenda: Agenda) = TopicDialog()
                .apply {
                    arguments = Bundle().apply { putParcelable(ARG_AGENDA, agenda) }
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_topic, null)
        setTypeFace()

        val agenda = arguments!!.getParcelable<Agenda>(ARG_AGENDA)

        with(mView) {
            btnFinish.setOnClickListener { dismiss() }
            tvTopic.text = agenda.theme
            tvDescription.text = agenda.description
            btnFinish
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvAgenda.typeface = boldTypeFace
            tvTopic.typeface = regularTypeFace
            tvDescription.typeface = regularTypeFace
        }
    }

}