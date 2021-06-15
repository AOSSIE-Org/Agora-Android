package org.aossie.agoraandroid.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions.Builder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.iv_back
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.tv_title
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.ActivityMainBinding
import org.aossie.agoraandroid.ui.fragments.elections.CalendarViewElectionFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
import org.aossie.agoraandroid.ui.fragments.settings.SettingsFragment
import org.aossie.agoraandroid.ui.fragments.welcome.WelcomeFragment
import org.aossie.agoraandroid.utilities.animGone
import org.aossie.agoraandroid.utilities.animVisible
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private lateinit var navController: NavController

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var mainFragmentFactory: FragmentFactory

  @Inject
  lateinit var prefs: PreferenceProvider

  private val viewModel: MainActivityViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    (application as AgoraApp).appComponent.inject(this)

    supportFragmentManager.fragmentFactory = mainFragmentFactory

    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    if (hostFragment is NavHostFragment)
      navController = hostFragment.navController

    navController.addOnDestinationChangedListener { _, destination, _ ->
      setToolbar(destination)
      handleBottomNavVisibility(destination.id)
      handleStatusBar(destination.id)
    }

    NavigationUI.setupWithNavController(bottom_navigation, navController)
    prefs.setUpdateNeeded(true)
    if (prefs.getIsLoggedIn()) {
      navController.navigate(R.id.homeFragment)
    }

    bottom_navigation.setOnNavigationItemReselectedListener {
      println()
    }

    viewModel.isLogout.observe(
      this,
      {
        if (it) logout()
      }
    )
  }

  private fun setToolbar(destination: NavDestination) {
    handleBackButton(destination.id)
    tv_title.text = destination.label
  }

  private fun handleBottomNavVisibility(id: Int) {
    when (id) {
      R.id.calendarViewElectionFragment,
      R.id.homeFragment,
      R.id.electionsFragment,
      R.id.settingsFragment
      -> bottom_navigation.animVisible()
      else -> bottom_navigation.animGone()
    }
  }

  private fun handleStatusBar(id: Int) {
    when (id) {
      R.id.welcomeFragment,
      R.id.loginFragment,
      R.id.signUpFragment,
      R.id.forgotPasswordFragment,
      R.id.settingsFragment
      -> {
        window.addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
        supportActionBar?.hide()
      }
      else -> {
        window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
        supportActionBar?.show()
      }
    }
  }

  override fun onBackPressed() {
    val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    if (hostFragment is NavHostFragment) {
      when (hostFragment.childFragmentManager.fragments.first()) {
        is HomeFragment,
        is WelcomeFragment -> finish()
        is SettingsFragment,
        is CalendarViewElectionFragment -> navController.navigate(R.id.homeFragment)
        else -> super.onBackPressed()
      }
    } else {
      super.onBackPressed()
    }
  }

  private fun handleBackButton(id: Int) {
    when (id) {
      R.id.aboutFragment,
      R.id.reportBugFragment,
      R.id.shareWithOthersFragment,
      R.id.contactUsFragment,
      R.id.profileFragment -> iv_back.let {
        it.visibility = View.VISIBLE
        it.setOnClickListener { onBackPressed() }
      }
      else -> iv_back.visibility = View.GONE
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    val childFragments = navHostFragment?.childFragmentManager?.fragments
    childFragments?.forEach { it.onActivityResult(requestCode, resultCode, data) }
  }

  fun logout() {
    if (prefs.getIsLoggedIn()) binding.root.snackbar(resources.getString(R.string.token_expired))
    if (prefs.getIsFacebookUser()) {
      LoginManager.getInstance()
        .logOut()
    }
    viewModel.deleteUserData()
    val navBuilder = Builder()
    navBuilder.setEnterAnim(R.anim.slide_in_left)
      .setExitAnim(R.anim.slide_out_right)
      .setPopEnterAnim(R.anim.slide_in_right)
      .setPopExitAnim(R.anim.slide_out_left)
    navController.navigate(R.id.welcomeFragment, null, navBuilder.build())
  }
}
