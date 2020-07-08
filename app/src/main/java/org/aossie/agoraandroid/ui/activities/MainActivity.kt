package org.aossie.agoraandroid.ui.activities

import android.app.ActionBar
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.iv_back
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.tv_title
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.elections.ElectionsFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
import org.aossie.agoraandroid.ui.fragments.moreOptions.MoreOptionsFragment
import org.aossie.agoraandroid.ui.fragments.profile.ProfileFragment
import org.aossie.agoraandroid.ui.fragments.welcome.WelcomeFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  private lateinit var navController: NavController

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var mainFragmentFactory: FragmentFactory

  @Inject
  lateinit var prefs: PreferenceProvider

  override fun onCreate(savedInstanceState: Bundle?) {

    (application as AgoraApp).appComponent.inject(this)

    supportFragmentManager.fragmentFactory = mainFragmentFactory

    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
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
  }

  private fun setToolbar(destination: NavDestination) {
    handleBackButton(destination.id)
    tv_title.text = destination.label
  }

  private fun handleBottomNavVisibility(id: Int) {
    when (id) {
      R.id.profileFragment,
      R.id.homeFragment,
      R.id.electionsFragment,
      R.id.moreOptionsFragment
      -> bottom_navigation.visibility = View.VISIBLE
      else -> bottom_navigation.visibility = View.GONE
    }
  }

  private fun handleStatusBar(id: Int) {
    when (id) {
      R.id.welcomeFragment,
      R.id.loginFragment,
      R.id.signUpFragment,
      R.id.forgotPasswordFragment
      -> window.addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
      else -> window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
  }

  override fun onBackPressed() {
    val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    if (hostFragment is NavHostFragment) {
      when (hostFragment.childFragmentManager.fragments.first()) {
        is HomeFragment,
        is WelcomeFragment -> moveTaskToBack(true)
        is ElectionsFragment,
        is ProfileFragment,
        is MoreOptionsFragment -> navController.navigate(R.id.homeFragment)
       else -> super.onBackPressed()
      }
    } else {
      super.onBackPressed()
    }
  }

  private fun handleBackButton(id: Int) {
    when (id) {
      R.id.loginFragment,
      R.id.signUpFragment,
      R.id.forgotPasswordFragment,
      R.id.aboutFragment,
      R.id.reportBugFragment,
      R.id.shareWithOthersFragment,
      R.id.contactUsFragment -> iv_back.let {
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
}

