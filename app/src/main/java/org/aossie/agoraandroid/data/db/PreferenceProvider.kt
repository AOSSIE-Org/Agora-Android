package org.aossie.agoraandroid.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

private const val IS_LOGGED_IN = "isLoggedIn"
private const val IS_FACEBOOK_USER = "isFacebookUser"
private const val IS_UPDATE_NEEDED = "isUpdateNeeded"
private const val ACCESS_TOKEN = "accessToken"
private const val REFRESH_TOKEN = "refreshToken"
private const val FACEBOOK_ACCESS_TOKEN = "facebookAccessToken"

class PreferenceProvider
@Inject
constructor(
  context: Context
) {
  private val appContext = context.applicationContext
  private val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)

  fun setIsLoggedIn(boolean: Boolean) {
    preferences.edit()
      .putBoolean(
        IS_LOGGED_IN,
        boolean
      )
      .apply()
  }

  fun getIsLoggedIn(): Boolean {
    return preferences.getBoolean(IS_LOGGED_IN, false)
  }

  fun setIsFacebookUser(boolean: Boolean) {
    preferences.edit()
      .putBoolean(
        IS_FACEBOOK_USER,
        boolean
      )
      .apply()
  }

  fun getIsFacebookUser(): Boolean {
    return preferences.getBoolean(IS_FACEBOOK_USER, false)
  }

  fun setUpdateNeeded(isNeeded: Boolean) {
    preferences.edit()
      .putBoolean(
        IS_UPDATE_NEEDED,
        isNeeded
      )
      .apply()
  }

  fun getUpdateNeeded(): Boolean {
    return preferences.getBoolean(IS_UPDATE_NEEDED, true)
  }

  fun setAccessToken(token: String?) {
    preferences.edit()
      .putString(
        ACCESS_TOKEN,
        token
      )
      .apply()
  }

  fun getAccessToken(): String? {
    return preferences.getString(ACCESS_TOKEN, null)
  }

  fun setRefreshToken(token: String?) {
    preferences.edit()
      .putString(
        REFRESH_TOKEN,
        token
      )
      .apply()
  }

  fun getRefreshToken(): String? {
    return preferences.getString(REFRESH_TOKEN, null)
  }

  fun setFacebookAccessToken(accessToken: String?) {
    preferences.edit()
      .putString(
        FACEBOOK_ACCESS_TOKEN,
        accessToken
      )
      .apply()
  }

  fun getFacebookAccessToken(): String? {
    return preferences.getString(FACEBOOK_ACCESS_TOKEN, null)
  }

  fun clearData() {
    preferences.edit()
      .clear()
      .apply()
  }
}
