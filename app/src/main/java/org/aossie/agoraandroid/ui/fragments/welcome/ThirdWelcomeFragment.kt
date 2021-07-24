package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.databinding.FragmentThirdWelcomeBinding

class ThirdWelcomeFragment : Fragment() {

  private lateinit var binding: FragmentThirdWelcomeBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentThirdWelcomeBinding.inflate(layoutInflater)

    return binding.root
  }

  companion object {
    fun newInstance() = ThirdWelcomeFragment()
  }
}
