package org.aossie.agoraandroid.ui.fragments.moreOptions

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_about
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_contact_us
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_dashboard
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_logout
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_report
import kotlinx.android.synthetic.main.fragment_more_options.view.tv_share
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class MoreOptionsFragment
  @Inject
  constructor(
    private val viewmodelFactory: ViewModelProvider.Factory
  ): Fragment(), AuthListener {

  private lateinit var rootView: View
  private val homeViewModel: HomeViewModel by viewModels {
    viewmodelFactory
  }
  private var loadToast: LoadToast? = null
  private var sharedPrefs: SharedPrefs? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_more_options, container, false)
    sharedPrefs = SharedPrefs(context!!)
    loadToast = LoadToast(context)

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
    loadToast!!.success()
    rootView.snackbar("Logged Out")
    Navigation.findNavController(rootView)
        .navigate(
            MoreOptionsFragmentDirections.actionMoreOptionsFragmentToWelcomeFragment()
        )
  }
  override fun onStarted() {
    loadToast!!.setText("Logging out")
    loadToast!!.show()
  }

  override fun onFailure(message: String) {
    loadToast!!.error()
    rootView.snackbar(message)
  }

}
