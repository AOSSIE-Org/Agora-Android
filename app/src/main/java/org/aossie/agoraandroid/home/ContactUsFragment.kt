package org.aossie.agoraandroid.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_us, null)
        val gitlabBtn = view.findViewById<Button>(R.id.button_gitlab)
        val gitterBtn = view.findViewById<Button>(R.id.button_gitter)
        gitterBtn.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://gitter.im/aossie/home"))) }
        gitlabBtn.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/aossie"))) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}