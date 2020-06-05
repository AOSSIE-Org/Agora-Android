package org.aossie.agoraandroid.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class PreferenceProvider
@Inject
constructor(
  context: Context
) {
  private val appContext = context.applicationContext
  private val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}