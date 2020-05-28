package org.aossie.agoraandroid.ui.fragments.moreOptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_about
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_contact_us
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_dashboard
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_logout
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_report
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_share
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.SharedPrefs

/**
 * A simple [Fragment] subclass.
 */
class MoreOptionsFragment : Fragment(), AuthListener {

  private lateinit var rootView: View
  private var homeViewModel: HomeViewModel? = null
  private var sharedPrefs: SharedPrefs? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_more_options, container, false)
    homeViewModel = HomeViewModel(
        activity!!.application, context
    )
    sharedPrefs = SharedPrefs(context!!)

    homeViewModel!!.authListener = this

    rootView.tv_share.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(
              MoreOptionsFragmentDirections.actionMoreOptionsFragmentToShareWithOthersFragment()
          )
    }

    rootView.tv_about.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(
              MoreOptionsFragmentDirections.actionMoreOptionsFragmentToAboutFragment()
          )
    }

    rootView.tv_dashboard.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(
              MoreOptionsFragmentDirections.actionMoreOptionsFragmentToHomeFragment()
          )
    }

    rootView.tv_contact_us.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(
              MoreOptionsFragmentDirections.actionMoreOptionsFragmentToContactUsFragment()
          )
    }

    rootView.tv_report.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(
              MoreOptionsFragmentDirections.actionMoreOptionsFragmentToReportBugFragment()
          )
    }

    rootView.tv_logout.setOnClickListener {
      homeViewModel!!.doLogout(sharedPrefs!!.token)
    }

    return rootView
  }

  override fun onSuccess() {
    Navigation.findNavController(rootView)
        .navigate(
            MoreOptionsFragmentDirections.actionMoreOptionsFragmentToWelcomeFragment()
        )
  }

}
