package org.zapomni.venturers.presentation.trip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.zapomni.venturers.R

class TripActivity : AppCompatActivity() {

    companion object {
        private val ACTION_WHAT_TAKES = "ACTION_WHAT_TAKES"

        private const val EXTRA_CONTENT = "EXTRA_CONTENT"
        private const val EXTRA_IMG_URL = "EXTRA_IMG_URL"

        fun startWhatTakes(activity: Activity?, content: String, imgUrl: String?) {
            activity?.startActivity(Intent(activity, TripActivity::class.java)
                    .setAction(ACTION_WHAT_TAKES)
                    .putExtra(EXTRA_CONTENT, content)
                    .putExtra(EXTRA_IMG_URL, imgUrl))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val fragment = when (intent.action) {
            ACTION_WHAT_TAKES -> WhatTakeFragment.newInstance(intent.getStringExtra(EXTRA_CONTENT), intent.getStringExtra(EXTRA_IMG_URL))
            else -> throw IllegalArgumentException("No such action")
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment, fragment::class.java.simpleName)
                .commit()
    }
}