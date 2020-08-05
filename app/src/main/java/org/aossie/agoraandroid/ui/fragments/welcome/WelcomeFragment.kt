package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_welcome.view.btn_login
import kotlinx.android.synthetic.main.fragment_welcome.view.btn_signup
import org.aossie.agoraandroid.R

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment: Fragment(){

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_welcome, container, false)
//    hideActionBar()

    rootView.btn_login.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
    }

    rootView.btn_signup.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment())
    }

    return rootView
  }

}
