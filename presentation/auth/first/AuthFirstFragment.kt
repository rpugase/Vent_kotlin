package org.zapomni.venturers.presentation.auth.first

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.fragment_auth_first.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.processPhoneNumber
import org.zapomni.venturers.extensions.watchText
import org.zapomni.venturers.presentation.auth.second.AuthSecondFragment
import org.zapomni.venturers.presentation.base.BaseFragment

class AuthFirstFragment : BaseFragment<AuthFirstView, AuthFirstPresenter>(), AuthFirstView {

    private var phoneWatcher: TextWatcher? = null
    private var unregistrar: Unregistrar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etPhone.requestFocus(etPhone.text.length)
        btnNext.setOnClickListener { presenter.login(etPhone.text.toString()) }
    }

    override fun onStart() {
        super.onStart()
        phoneWatcher = etPhone.watchText {
            if (it.length < 5) {
                etPhone.setText("+380 ")
                etPhone.setSelection(etPhone.length())
            }

            if (it.processPhoneNumber().length > 12) {
                etPhone.setText(it.dropLast(1))
                etPhone.setSelection(etPhone.length())
            }
        }

        unregistrar = KeyboardVisibilityEvent.registerEventListener(activity) {
            val heightKeyboard = (root.rootView.height - root.height).toFloat()
            root.translationY = if (it) -heightKeyboard else 0f
        }
    }

    override fun onStop() {
        unregistrar?.unregister()
        etPhone.removeTextChangedListener(phoneWatcher)
        super.onStop()
    }

    override fun provideLayoutId() = R.layout.fragment_auth_first

    override fun createPresenter() = inject<AuthFirstPresenter>().value

    override fun nextStep() {
        fragmentManager?.also {
            it.beginTransaction()
                    .replace(R.id.container, AuthSecondFragment.newInstance(etPhone.text.toString()), AuthSecondFragment::class.java.name)
                    .addToBackStack(AuthSecondFragment::class.java.name)
                    .commit()
        }
    }

    override fun showPhoneNumberError(resId: Int) {
        etPhone.requestFocus()
        etPhone.error = getString(resId)
    }

    override fun setTypeFace() {
        tvRegistration.typeface = boldTypeFace
        tvUsePhone.typeface = mediumTypeFace
        btnNext.typeface = boldTypeFace
        tvPhone.typeface = semiBoldTypeFace
        etPhone.typeface = regularTypeFace
    }
}