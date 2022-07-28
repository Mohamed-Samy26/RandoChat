package com.osc.randochat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.osc.randochat.R
import com.osc.randochat.databinding.FragmentLoginBinding
import com.osc.randochat.ui.login.Verify
import org.w3c.dom.Text


class Login : Fragment(com.osc.randochat.R.layout.fragment_login) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(
            com.osc.randochat.R.layout.fragment_login,
            container, false
        )
        val btn = view.findViewById<Button>(R.id.enter_btn)
        val txt = view.findViewById<TextInputEditText>(R.id.enter_phone)

        btn.setOnClickListener {
            println("Heeeeeeeeeeeeeeeere" + txt.text.toString())

            if (txt.text.toString().length >= 7){
                println("Heeeeeeeeeeeeeeeere")
                val intent = Intent(activity , Verify::class.java)
                intent.putExtra("phone" , txt.text.toString())
                startActivity(intent)
            }
        }
        return view
    }
}