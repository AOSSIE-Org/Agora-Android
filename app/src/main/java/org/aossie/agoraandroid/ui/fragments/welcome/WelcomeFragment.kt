package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

  private lateinit var binding: FragmentWelcomeBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
//    hideActionBar()

    binding.btnLogin.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
    }

    binding.btnSignup.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment())
    }

    return binding.root
  }
}
