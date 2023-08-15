package org.aossie.agoraandroid.ui.activities.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions.Builder
import androidx.navigation.fragment.NavHostFragment
import com.facebook.login.LoginManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.ActivityMainBinding
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.AppBarVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.BackBtnVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.BottomNavVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.ShowTitle
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityNavigationBar
import org.aossie.agoraandroid.ui.fragments.elections.CalendarViewElectionFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
import org.aossie.agoraandroid.ui.fragments.settings.SettingsFragment
import org.aossie.agoraandroid.ui.fragments.welcome.WelcomeFragment
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.canAuthenticateBiometric
import org.aossie.agoraandroid.utilities.notifyNetworkChanged
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private lateinit var navController: NavController
  private var isInitiallyNetworkConnected: Boolean = true

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var mainFragmentFactory: FragmentFactory

  @Inject
  lateinit var prefs: PreferenceProvider

  private val viewModel: MainActivityViewModel by viewModels {
    viewModelFactory
  }

  private val navDestinationLiveData = mutableStateOf<NavDestination?>(null)

  override fun onCreate(savedInstanceState: Bundle?) {

    (application as AgoraApp).appComponent.inject(this)

    supportFragmentManager.fragmentFactory = mainFragmentFactory

    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    intent?.getStringExtra(AppConstants.SHOW_SNACKBAR_KEY)
      ?.let {
        binding.root.snackbar(it)
      }
    val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    if (hostFragment is NavHostFragment)
      navController = hostFragment.navController

    navController.addOnDestinationChangedListener { _, destination, _ ->
      navDestinationLiveData.value = destination
      setToolbar(destination)
      handleBottomNavVisibility(destination.id)
      handleStatusBar(destination.id)
    }

    GlobalScope.launch {
      prefs.setUpdateNeeded(true)
      if (prefs.getIsLoggedIn()
          .first()
      ) {
        if (prefs.isBiometricEnabled().first() && canAuthenticateBiometric())
          withContext(Dispatchers.Main) { provideOfBiometricPrompt().authenticate(getPromtInfo()) }
        else
          withContext(Dispatchers.Main) { navController.navigate(id.homeFragment) }
      }
    }

    initObservers()
    loadComposeView()
  }

  @OptIn(ExperimentalMaterial3Api::class)
  private fun loadComposeView() {
    binding.toolbar.setContent {
      AgoraTheme {
        AnimatedVisibility(
          visible = viewModel.mainActivityState.value.isAppBarVisible) {
          TopAppBar(
            title = {
              Box(modifier =  if(!viewModel.mainActivityState.value.isBackBtnVisible){
                Modifier
                  .animateContentSize()
                  .fillMaxWidth()
              }else {
                Modifier.animateContentSize()
              },
                contentAlignment = Alignment.Center
              ){
                Text(
                  text = viewModel.mainActivityState.value.title,
                )
              }
            },
            navigationIcon = {
              AnimatedVisibility(
                visible = viewModel.mainActivityState.value.isBackBtnVisible,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                  -fullWidth
                } + fadeIn(
                  animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                  -fullWidth
                }
              ) {
                IconButton(onClick = {
                  navController.popBackStack()
                }) {
                  Icon(
                    imageVector = Rounded.ArrowBackIosNew,
                    contentDescription = "Arrow Back",
                    tint = MaterialTheme.colorScheme.onBackground
                  )
                }
              }
            },
            colors = TopAppBarDefaults.topAppBarColors(
              containerColor = MaterialTheme.colorScheme.background,
              navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
              titleContentColor = MaterialTheme.colorScheme.onBackground
            )
          )
        }
      }
    }
    binding.bottomNavigation.setContent {
      val itemsList = listOf(
        stringResource(id = R.string.home) to R.drawable.ic_home,
        stringResource(id = R.string.calendar) to R.drawable.ic_calendar,
        stringResource(id = R.string.settings) to R.drawable.ic_settings,
      )
      val navDestination by navDestinationLiveData
      val currentSelected = remember {
        mutableStateOf(0)
      }
      LaunchedEffect(key1 = navDestination) {
        navDestination?.id.let {
          when(it) {
            id.homeFragment -> {
              currentSelected.value = 0
            }
            id.calendarViewElectionFragment -> {
              currentSelected.value = 1
            }
            id.settingsFragment -> {
              currentSelected.value = 2
            }
        }
      }
      }

      AgoraTheme {
        AnimatedVisibility(
          visible = viewModel.mainActivityState.value.isBottomNavVisible,
          enter = slideInVertically(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            fullWidth
          } + fadeIn(
            animationSpec = tween(durationMillis = 200)
          ),
          exit = slideOutVertically(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            fullWidth
          }
        ) {
          MainActivityNavigationBar(
            currentSelected = currentSelected.value,
            itemsList = itemsList
          ) {
            currentSelected.value = it
            when(it) {
              0 -> {
                navController.navigate(id.homeFragment)
              }
              1 -> {
                navController.navigate(id.calendarViewElectionFragment)
              }
              else -> {
                navController.navigate(id.settingsFragment)
              }
            }
          }
        }
      }
    }
  }

  private fun getPromtInfo() =
    PromptInfo.Builder()
      .setTitle(getString(string.bio_auth))
      .setDescription(getString(string.bio_auth_msg))
      .apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
          setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG or Authenticators.BIOMETRIC_WEAK or Authenticators.DEVICE_CREDENTIAL)
        else {
          setDeviceCredentialAllowed(true)
          setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG or Authenticators.BIOMETRIC_WEAK)
        }
      }.build()

  private fun provideOfBiometricPrompt(): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(this)

    val callback = object : BiometricPrompt.AuthenticationCallback() {
      override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        binding.root.snackbar(getString(string.something_went_wrong))
      }

      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        binding.root.snackbar(getString(string.auth_failed))
      }

      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        navController.navigate(R.id.homeFragment)
      }
    }

    return BiometricPrompt(this, executor, callback)
  }

  private fun setToolbar(destination: NavDestination) {
    handleBackButton(destination.id)
    viewModel.onEvent(ShowTitle(destination.label.toString()))
  }

  private fun handleBottomNavVisibility(id: Int) {
    when (id) {
      R.id.calendarViewElectionFragment,
      R.id.homeFragment,
      R.id.settingsFragment -> viewModel.onEvent(BottomNavVisibility(true))
      else -> viewModel.onEvent(BottomNavVisibility(false))
    }
  }

  private fun handleStatusBar(id: Int) {
    when (id) {
      R.id.welcomeFragment,
      R.id.loginFragment,
      R.id.signUpFragment,
      R.id.forgotPasswordFragment,
      R.id.twoFactorAuthFragment
      -> {
        viewModel.onEvent(AppBarVisibility(false))
      }
      else -> {
        viewModel.onEvent(AppBarVisibility(true))
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
      R.id.homeFragment,
      R.id.calendarViewElectionFragment,
      R.id.settingsFragment-> viewModel.onEvent(BackBtnVisibility(false))
      else -> viewModel.onEvent(BackBtnVisibility(true))
    }
  }

  private fun initObservers() {
    lifecycleScope.launch {
      viewModel.getNetworkStatusStateFlow.collect { isConnected ->
        if (isConnected == true) {
          if (!isInitiallyNetworkConnected)
            binding.root.notifyNetworkChanged(true, binding.bottomNavigation)
          isInitiallyNetworkConnected = true
        } else {
          isInitiallyNetworkConnected = false
          binding.root.notifyNetworkChanged(false, binding.bottomNavigation)
        }
      }
    }
    lifecycleScope.launch {
      viewModel.isLogout.collect {
        if (it == true) logout()
      }
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

  private fun logout() {
    lifecycleScope.launch {
      if (prefs.getIsLoggedIn()
          .first()
      ) binding.root.snackbar(resources.getString(R.string.token_expired))
      if (prefs.getIsFacebookUser().first()) {
        LoginManager.getInstance()
          .logOut()
      }
    }
    viewModel.deleteUserData()
    val navBuilder = Builder()
    navBuilder.setEnterAnim(R.anim.slide_in_left)
      .setExitAnim(R.anim.slide_out_right)
      .setPopEnterAnim(R.anim.slide_in_right)
      .setPopExitAnim(R.anim.slide_out_left)
    navController.navigate(R.id.welcomeFragment, null, navBuilder.build())
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.getStringExtra(AppConstants.SHOW_SNACKBAR_KEY)
      ?.let {
        binding.root.snackbar(it)
      }
  }
}