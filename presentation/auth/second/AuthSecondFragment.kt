package org.zapomni.venturers.presentation.auth.second

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_auth_second.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.auth.third.AuthThirdFragment
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.main.MainActivity

class AuthSecondFragment : BaseFragment<AuthSecondView, AuthSecondPresenter>(), AuthSecondView {

    companion object {
        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
        fun newInstance(phoneNumber: String): AuthSecondFragment {
            return AuthSecondFragment().apply {
                arguments = Bundle().apply { putString(KEY_PHONE_NUMBER, phoneNumber) }
            }
        }
    }

    private var unregistrar: Unregistrar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.phoneNumber = arguments?.getString(KEY_PHONE_NUMBER)

        btnNext.setOnClickListener { presenter.checkCode(etCode.text.toString()) }
        btnSendCode.setOnClickListener { presenter.sendCodeAgain() }
    }

    override fun onStart() {
        super.onStart()
        unregistrar = KeyboardVisibilityEvent.registerEventListener(activity, {
            val heightKeyboard = (root.rootView.height - root.height).toFloat()
            root.translationY = if (it) -heightKeyboard else 0f
        })
    }

    override fun onStop() {
        unregistrar?.unregister()
        super.onStop()
    }

    override fun finishAuth() {
        MainActivity.start(context!!)
        activity?.finish()
    }

    override fun nextStep() {
        fragmentManager?.also {
            it.beginTransaction()
                    .replace(R.id.container, AuthThirdFragment(), AuthThirdFragment::class.java.name)
                    .commit()
        }
    }

    override fun showTimer(time: Long) {
        if (time == -1L) btnSendCode.text = getString(R.string.send_again)
        else btnSendCode.text = getString(R.string.send_again_with_time, time)
    }

    override fun showErrorForCode(resId: Int) {
        etCode.error = getString(resId)
    }

    override fun setTypeFace() {
        tvRegistration.typeface = boldTypeFace
        tvUsePhone.typeface = mediumTypeFace
        btnNext.typeface = boldTypeFace
        tvCode.typeface = semiBoldTypeFace
        etCode.typeface = regularTypeFace
        btnSendCode.typeface = regularTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_auth_second

    override fun createPresenter() = inject<AuthSecondPresenter>().value
}