package org.aossie.agoraandroid.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

private const val IS_LOGGED_IN = "isLoggedIn"

class PreferenceProvider
@Inject
constructor(
  context: Context
) {
  private val appContext = context.applicationContext
  private val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)

  fun setIsLoggedIn(boolean: Boolean) {
    preferences.edit().putBoolean(
        IS_LOGGED_IN,
        boolean
    ).apply()
  }

  fun getIsLoggedIn(): Boolean {
    return preferences.getBoolean(IS_LOGGED_IN, false)
  }
}