package org.aossie.agoraandroid.ui.fragments.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentReportBugBinding

/**
 * A simple [Fragment] subclass.
 */
class ReportBugFragment : Fragment() {

  private lateinit var binding: FragmentReportBugBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_bug, container, false)
    binding.buttonReportBug.setOnClickListener {
      startActivity(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://gitlab.com/aossie/agora-android/issues/new")
        )
      )
    }
    return binding.root
  }
}
