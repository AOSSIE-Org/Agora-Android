package org.aossie.agoraandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.home.HomeActivity
import org.aossie.agoraandroid.utilities.SharedPrefs

class MainActivity : AppCompatActivity() {

  private lateinit var navController: NavController
  private lateinit var sharedPrefs: SharedPrefs

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    sharedPrefs = SharedPrefs(this@MainActivity)

    val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)
    if (hostFragment is NavHostFragment)
      navController = hostFragment.navController

    val userName = sharedPrefs.userName
    val password = sharedPrefs.pass
    if (userName != null && password != null) {
      startActivity(Intent(this@MainActivity, HomeActivity::class.java))
      finish()
    }
  }

}