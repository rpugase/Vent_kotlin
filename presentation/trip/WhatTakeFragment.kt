package org.zapomni.venturers.presentation.trip

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_what_take.*
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.loadImage

class WhatTakeFragment : Fragment() {

    companion object {

        private const val ARG_CONTENT = "ARG_CONTENT"
        private const val ARG_IMG_URL = "ARG_IMG_URL"

        fun newInstance(content: String, imgUrl: String?) = WhatTakeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CONTENT, content)
                putString(ARG_IMG_URL, imgUrl)
            }
        }
    }

    private var boldTypeFace: Typeface? = null
    private var regularTypeFace: Typeface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_what_take, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        boldTypeFace = ResourcesCompat.getFont(context!!, R.font.gotha_pro_bold)
        regularTypeFace = ResourcesCompat.getFont(context!!, R.font.source_sans_pro_regular)

        btnUnderstand.setOnClickListener { activity?.onBackPressed() }

        btnUnderstand.typeface = boldTypeFace
        tvWhatTakes.typeface = boldTypeFace
        tvContent.typeface = regularTypeFace

//        tvContent.text = Html.fromHtml(arguments?.getString(ARG_CONTENT))
        tvContent.text = arguments?.getString(ARG_CONTENT) // todo replace to html parse
        imgTop.loadImage(arguments?.getString(ARG_IMG_URL))
    }
}