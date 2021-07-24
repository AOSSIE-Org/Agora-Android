package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.databinding.FragmentFirstWelcomeBinding

class FirstWelcomeFragment : Fragment() {

  private lateinit var binding: FragmentFirstWelcomeBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentFirstWelcomeBinding.inflate(layoutInflater)

    return binding.root
  }

  companion object {
    fun newInstance() = FirstWelcomeFragment()
  }
}