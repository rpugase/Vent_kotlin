package org.zapomni.venturers.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.auth.first.AuthFirstFragment
import org.zapomni.venturers.presentation.auth.third.AuthThirdFragment
import org.zapomni.venturers.presentation.base.BaseActivity

class AuthActivity : BaseActivity<AuthView, AuthPresenter>(), AuthView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun createPresenter(): AuthPresenter = inject<AuthPresenter>().value

    override fun loadFirstStep() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, AuthFirstFragment(), AuthFirstFragment::class.java.name)
                .commit()
    }

    override fun loadThirdStep() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, AuthThirdFragment(), AuthThirdFragment::class.java.name)
                .commit()
    }
}