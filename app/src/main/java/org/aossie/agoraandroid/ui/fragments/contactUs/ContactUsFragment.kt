package org.aossie.agoraandroid.ui.fragments.contactUs

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.databinding.FragmentContactUsBinding
import org.aossie.agoraandroid.utilities.browse
import org.aossie.agoraandroid.utilities.snackbar

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {

  lateinit var binding: FragmentContactUsBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentContactUsBinding.inflate(layoutInflater)
    initListeners()
    return binding.root
  }

  private fun initListeners() {
    binding.btnGitter.setOnClickListener {
      openUrl("https://gitter.im/aossie/home")
    }
    binding.btnGitlab.setOnClickListener {
      openUrl("https://gitlab.com/aossie")
    }
    binding.btnReport.setOnClickListener {
      openUrl("https://gitlab.com/aossie/agora-android/issues/new")
    }
  }

  private fun openUrl(url: String) {
    try {
      context?.browse(url)
    } catch (e: ActivityNotFoundException) {
      binding.root.snackbar(resources.getString(string.no_browser))
    }
  }
}
