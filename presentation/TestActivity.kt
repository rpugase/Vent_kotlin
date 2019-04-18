package org.zapomni.venturers.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import org.zapomni.venturers.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        window.statusBarColor = Color.parseColor("#464241")
        lavRoo.setOnClickListener { lavRoo.playAnimation() }
    }
}