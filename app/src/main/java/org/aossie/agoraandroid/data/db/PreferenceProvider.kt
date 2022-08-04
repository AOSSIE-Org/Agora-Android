package org.aossie.agoraandroid.data.db

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.aossie.agoraandroid.common.utilities.SecurityUtil
import org.aossie.agoraandroid.common.utilities.spotlightDataStore
import org.aossie.agoraandroid.common.utilities.userDataStore
import javax.inject.Inject

class PreferenceProvider
@Inject
constructor(
  context: Context,
  private val securityUtil: SecurityUtil
) {

  companion object {
    private val MAIL_ID = stringPreferencesKey("mailId")
    private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    private val IS_FACEBOOK_USER = booleanPreferencesKey("isFacebookUser")
    private val IS_UPDATE_NEEDED = booleanPreferencesKey("isUpdateNeeded")
    private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    private val FACEBOOK_ACCESS_TOKEN = stringPreferencesKey("facebookAccessToken")
    private val ENABLE_BIOMETRIC = booleanPreferencesKey("isBiometricEnabled")
  }

  private val userDataStore = context.userDataStore
  private val spotlightDataStore = context.spotlightDataStore

  fun isDisplayed(id: String): Flow<Boolean> {
    return spotlightDataStore.data.map {
      it[booleanPreferencesKey(id)] ?: false
    }
  }

  suspend fun setDisplayed(id: String) {
    spotlightDataStore.edit {
      it[booleanPreferencesKey(id)] = true
    }
  }

  suspend fun setMailId(mailId: String) {
    userDataStore.edit {
      it[MAIL_ID] = mailId
    }
  }

  fun getMailId(): Flow<String?> {
    return userDataStore.data.map {
      it[MAIL_ID]
    }
  }

  suspend fun setIsLoggedIn(boolean: Boolean) {
    userDataStore.edit {
      it[IS_LOGGED_IN] = boolean
    }
  }
  suspend fun enableBiometric(boolean: Boolean) {
    userDataStore.edit {
      it[ENABLE_BIOMETRIC] = boolean
    }
  }

  fun isBiometricEnabled(): Flow<Boolean> {
    return userDataStore.data.map {
      it[ENABLE_BIOMETRIC] ?: false
    }
  }
  fun getIsLoggedIn(): Flow<Boolean> {
    return userDataStore.data.map {
      it[IS_LOGGED_IN] ?: false
    }
  }

  suspend fun setIsFacebookUser(boolean: Boolean) {
    userDataStore.edit {
      it[IS_FACEBOOK_USER] = boolean
    }
  }

  fun getIsFacebookUser(): Flow<Boolean> {
    return userDataStore.data.map {
      it[IS_FACEBOOK_USER] ?: false
    }
  }

  suspend fun setUpdateNeeded(isNeeded: Boolean) {
    userDataStore.edit {
      it[IS_UPDATE_NEEDED] = isNeeded
    }
  }

  fun getUpdateNeeded(): Flow<Boolean> {
    return userDataStore.data.map {
      it[IS_UPDATE_NEEDED] ?: true
    }
  }

  suspend fun setAccessToken(token: String?) {
    userDataStore.edit {
      it[ACCESS_TOKEN] = token?.let { _token ->
        securityUtil.encryptToken(_token)
      } ?: ""
    }
  }

  fun getAccessToken(): Flow<String?> {
    return userDataStore.data.map {
      it[ACCESS_TOKEN]?.let { _token ->
        securityUtil.decryptToken(_token)
      }
    }
  }

  suspend fun setRefreshToken(token: String?) {
    userDataStore.edit {
      it[REFRESH_TOKEN] = token?.let { _token ->
        securityUtil.encryptToken(_token)
      } ?: ""
    }
  }

  fun getRefreshToken(): Flow<String?> {
    return userDataStore.data.map {
      it[REFRESH_TOKEN]?.let { _token ->
        securityUtil.decryptToken(_token)
      }
    }
  }

  suspend fun setFacebookAccessToken(accessToken: String?) {
    userDataStore.edit {
      it[FACEBOOK_ACCESS_TOKEN] = accessToken?.let { _token ->
        securityUtil.encryptToken(_token)
      } ?: ""
    }
  }

  fun getFacebookAccessToken(): Flow<String?> {
    return userDataStore.data.map {
      it[FACEBOOK_ACCESS_TOKEN]?.let { _token ->
        securityUtil.decryptToken(_token)
      }
    }
  }

  suspend fun clearAllData() {
    clearUserData()
    clearSpotlightData()
  }

  private suspend fun clearUserData() {
    userDataStore.edit {
      it.clear()
    }
  }

  private suspend fun clearSpotlightData() {
    spotlightDataStore.edit {
      it.clear()
    }
  }
}
