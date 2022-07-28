package com.osc.randochat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.osc.randochat.R
import com.osc.randochat.databinding.FragmentJoinChatBinding


class JoinChat : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentJoinChatBinding.inflate(inflater)
        binding.callBtn.setOnClickListener {

        }

        return inflater.inflate(R.layout.fragment_join_chat, container, false)
    }
}