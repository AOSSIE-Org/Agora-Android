package org.aossie.agoraandroid.ui.fragments.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_report_bug.view.button_report_bug
import org.aossie.agoraandroid.R.layout

/**
 * A simple [Fragment] subclass.
 */
class ReportBugFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_report_bug, container,false)
    view.button_report_bug.setOnClickListener {
      startActivity(
          Intent(
              Intent.ACTION_VIEW,
              Uri.parse("https://gitlab.com/aossie/agora-android/issues/new")
          )
      )
    }
    return view
  }
}