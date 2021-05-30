package org.aossie.agoraandroid.ui.fragments.contactUs

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.layout
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
  ): View? {
    binding = DataBindingUtil.inflate(inflater, layout.fragment_contact_us, container, false)
    initListeners()
    return binding.root
  }

  private fun initListeners() {
    binding.btnGitter.setOnClickListener {
      try {
        context?.browse("https://gitter.im/aossie/home")
      } catch (e: ActivityNotFoundException) {
        binding.root.snackbar(resources.getString(R.string.no_browser))
      }
    }
    binding.btnGitlab.setOnClickListener {
      try {
        context?.browse("https://gitlab.com/aossie")
      } catch (e: ActivityNotFoundException) {
        binding.root.snackbar(resources.getString(R.string.no_browser))
      }
    }
    binding.btnReport.setOnClickListener {
      try {
        context?.browse("https://gitlab.com/aossie/agora-android/issues/new")
      } catch (e: ActivityNotFoundException) {
        binding.root.snackbar(resources.getString(R.string.no_browser))
      }
    }
  }
}
