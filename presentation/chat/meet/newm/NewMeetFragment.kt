package org.zapomni.venturers.presentation.chat.meet.newm

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_new_meet.*
import org.jetbrains.anko.longToast
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Meet
import org.zapomni.venturers.extensions.formatWithSimpleDateMeet
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.chat.meet.MeetActivity
import java.util.Calendar.*

class NewMeetFragment : BaseFragment<NewMeetView, NewMeetPresenter>(), NewMeetView {

    override fun provideLayoutId() = R.layout.fragment_new_meet
    override fun createPresenter() = inject<NewMeetPresenter>().value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnMakeMeet.setOnClickListener { presenter.makeMeet(etName.text.toString(), etPlace.text.toString()) }

        etDate.setOnClickListener {
            val now = getInstance()
            DatePickerDialog(context, R.style.PickerDialog, { _, year, month, dayOfMonth ->
                presenter.dateOfMeeting.apply {
                    TimePickerDialog(context, R.style.PickerDialog, { _, hourOfDay, minute ->
                        presenter.dateOfMeeting.apply {
                            set(YEAR, year)
                            set(MONTH, month)
                            set(DAY_OF_MONTH, dayOfMonth)
                            set(HOUR_OF_DAY, hourOfDay)
                            set(MINUTE, minute)
                        }
                        presenter.feltDateOfMeeting = true
                        etDate.setText(presenter.dateOfMeeting.time.formatWithSimpleDateMeet())
                    }, now.get(HOUR_OF_DAY), now.get(MINUTE), true).show()
                }
            }, now.get(YEAR), now.get(MONTH), now.get(DAY_OF_MONTH)).show()
        }
    }

    override fun returnWithTopic(meet: Meet) {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().putExtra(MeetActivity.EXTRA_MEET, meet))
            it.finish()
        }
    }

    override fun onNameError() {
        etName.requestFocus()
        etName.error = getString(R.string.error_empty_field)
    }

    override fun onPlaceError() {
        etPlace.requestFocus()
        etPlace.error = getString(R.string.error_empty_field)
    }

    override fun onDateError() {
        etDate.requestFocus()
        etDate.error = getString(R.string.error_empty_field)
    }

    override fun onErrorEarlyDate() {
        etDate.performClick()
        context?.longToast(getString(R.string.error_too_early))
    }

    override fun setTypeFace() {
        tvMakeMeet.typeface = boldTypeFace
        tvMeetWeek.typeface = regularTypeFace
        tvName.typeface = semiBoldTypeFace
        etName.typeface = regularTypeFace
        tvPlace.typeface = semiBoldTypeFace
        etPlace.typeface = regularTypeFace
        tvDate.typeface = semiBoldTypeFace
        etDate.typeface = regularTypeFace
        btnMakeMeet.typeface = boldTypeFace
        tvAboutPhone.typeface = regularTypeFace
    }
}