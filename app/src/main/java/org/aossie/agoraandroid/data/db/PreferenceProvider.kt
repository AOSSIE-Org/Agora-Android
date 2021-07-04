package org.aossie.agoraandroid.data.db

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.aossie.agoraandroid.utilities.dataStore
import javax.inject.Inject

class PreferenceProvider
@Inject
constructor(
  context: Context,
) {

  companion object {
    private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    private val IS_FACEBOOK_USER = booleanPreferencesKey("isFacebookUser")
    private val IS_UPDATE_NEEDED = booleanPreferencesKey("isUpdateNeeded")
    private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    private val FACEBOOK_ACCESS_TOKEN = stringPreferencesKey("facebookAccessToken")
  }

  private val dataStore = context.dataStore

  suspend fun setIsLoggedIn(boolean: Boolean) {
    dataStore.edit {
      it[IS_LOGGED_IN] = boolean
    }
  }

  fun getIsLoggedIn(): Flow<Boolean> {
    return dataStore.data.map {
      it[IS_LOGGED_IN] ?: false
    }
  }

  suspend fun setIsFacebookUser(boolean: Boolean) {
    dataStore.edit {
      it[IS_FACEBOOK_USER] = boolean
    }
  }

  fun getIsFacebookUser(): Flow<Boolean> {
    return dataStore.data.map {
      it[IS_FACEBOOK_USER] ?: false
    }
  }

  suspend fun setUpdateNeeded(isNeeded: Boolean) {
    dataStore.edit {
      it[IS_UPDATE_NEEDED] = isNeeded
    }
  }

  fun getUpdateNeeded(): Flow<Boolean> {
    return dataStore.data.map {
      it[IS_UPDATE_NEEDED] ?: true
    }
  }

  suspend fun setAccessToken(token: String?) {
    dataStore.edit {
      it[ACCESS_TOKEN] = token ?: ""
    }
  }

  fun getAccessToken(): Flow<String?> {
    return dataStore.data.map {
      it[ACCESS_TOKEN]
    }
  }

  suspend fun setRefreshToken(token: String?) {
    dataStore.edit {
      it[REFRESH_TOKEN] = token ?: ""
    }
  }

  fun getRefreshToken(): Flow<String?> {
    return dataStore.data.map {
      it[REFRESH_TOKEN]
    }
  }

  suspend fun setFacebookAccessToken(accessToken: String?) {
    dataStore.edit {
      it[FACEBOOK_ACCESS_TOKEN] = accessToken ?: ""
    }
  }

  fun getFacebookAccessToken(): Flow<String?> {
    return dataStore.data.map {
      it[FACEBOOK_ACCESS_TOKEN]
    }
  }

  suspend fun clearData() {
    dataStore.edit {
      it.clear()
    }
  }
}
