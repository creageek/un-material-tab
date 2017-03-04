package com.creageek.unmaterialtabs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_test, container, false)
        val text = rootView.findViewById(R.id.fragment_text) as TextView
        text.text = arguments.getCharSequence("title")
        return rootView
    }
}