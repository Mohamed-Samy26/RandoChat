package com.osc.randochat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.osc.randochat.R


class JoinChat : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_join_chat, container, false)
        val btn = view.findViewById<Button>(R.id.call_btn)
        val tv = view.findViewById<TextView>(R.id.call_size)
        btn.setOnClickListener {
            Toast.makeText(activity, tv.text.toString(), Toast.LENGTH_SHORT).show()
        }
        return view
      }
}