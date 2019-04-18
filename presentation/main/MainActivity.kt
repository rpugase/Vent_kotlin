package org.zapomni.venturers.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseActivity
import org.zapomni.venturers.presentation.chat.ChatFragment
import org.zapomni.venturers.presentation.event.list.EventListFragment
import org.zapomni.venturers.presentation.home.HomeFragment
import org.zapomni.venturers.presentation.trip.TripFragment

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val FRAGMENT_TAG = "MainActivityTag"

    private val fragments = arrayOf(HomeFragment(), ChatFragment(), EventListFragment(), TripFragment())
    private val menuItems = mapOf(R.id.aMain to 0, R.id.aChat to 1, R.id.aEvent to 2, R.id.aAdventure to 3)

    private var navigationHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBottom.onCheckedChangeListener = {
            val fragment = fragments[it]
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun showNavBottom() {
        navBottom.visibility = View.VISIBLE
        val fragment = fragments[menuItems[R.id.aChat]!!] as ChatFragment
        fragment.showFab()
    }

    override fun hideNavBottom() {
        navBottom.visibility = View.GONE
        val fragment = fragments[menuItems[R.id.aChat]!!] as ChatFragment
        fragment.showMessageBar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentByTag(FRAGMENT_TAG).onActivityResult(requestCode, resultCode, data)
    }

    override fun createPresenter() = inject<MainPresenter>().value

    fun goChat() {
        navBottom.buttons[1].isChecked = true
    }

    fun showNavigationBottom() {
        navBottom.layoutParams = navBottom.layoutParams.apply { height = LinearLayout.LayoutParams.WRAP_CONTENT }
    }

    fun hideNavigationBottom() {
        navBottom.layoutParams = navBottom.layoutParams.apply { height = 0 }
    }
}
