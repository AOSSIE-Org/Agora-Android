package org.aossie.agoraandroid.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

private const val IS_LOGGED_IN = "isLoggedIn"
private const val IS_UPDATE_NEEDED = "isUpdateNeeded"
private const val ACCESS_TOKEN = "token"

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

  fun setUpdateNeeded(isNeeded : Boolean){
    preferences.edit().putBoolean(
        IS_UPDATE_NEEDED,
        isNeeded)
        .apply()
  }

  fun getUpdateNeeded() : Boolean{
    return preferences.getBoolean(IS_UPDATE_NEEDED, true)
  }

  fun setCurrentToken(token: String?) {
    preferences.edit().putString(
        ACCESS_TOKEN,
        token
    ).apply()

  }

  fun getCurrentToken() : String?{
    return preferences.getString(ACCESS_TOKEN, null)
  }

  fun clearData(){
    preferences.edit().clear().apply()
  }


}