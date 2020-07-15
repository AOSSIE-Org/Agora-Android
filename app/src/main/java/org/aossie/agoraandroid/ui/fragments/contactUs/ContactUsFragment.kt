package org.aossie.agoraandroid.ui.fragments.contactUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_contact_us.view.btn_gitlab
import kotlinx.android.synthetic.main.fragment_contact_us.view.btn_gitter
import kotlinx.android.synthetic.main.fragment_contact_us.view.btn_report
import org.aossie.agoraandroid.R.layout

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_contact_us, container, false)
    view.btn_gitter.setOnClickListener {
      startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse("https://gitter.im/aossie/home"))
      )
    }
    view.btn_gitlab.setOnClickListener {
      startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/aossie"))
      )
    }
    view.btn_report.setOnClickListener {
      startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/aossie/agora-android/issues/new"))
      )
    }
    return view
  }
}