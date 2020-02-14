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
class ReportBugFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_report_bug, null)
        val openConsole = view.findViewById<Button>(R.id.button_report_bug)
        openConsole.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/aossie/agora-android/issues/new"))) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}