package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.databinding.FragmentSecondWelcomeBinding

class SecondWelcomeFragment : Fragment() {

  private lateinit var binding: FragmentSecondWelcomeBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentSecondWelcomeBinding.inflate(layoutInflater)

    return binding.root
  }

  companion object {
    fun newInstance() = SecondWelcomeFragment()
  }
}
