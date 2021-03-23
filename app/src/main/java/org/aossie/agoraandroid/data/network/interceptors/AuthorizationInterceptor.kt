package org.aossie.agoraandroid.data.network.interceptors

import timber.log.Timber
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.Client
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.AppConstants.ACCEPT
import org.aossie.agoraandroid.utilities.AppConstants.APPLICATION_JSON
import org.aossie.agoraandroid.utilities.AppConstants.CONTENT_TYPE
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.json.JSONObject
import java.io.IOException

class AuthorizationInterceptor(
  private val prefs: PreferenceProvider,
  private val appDatabase: AppDatabase,
  private val client: Client
) : Interceptor, ApiRequest() {

  override fun intercept(chain: Interceptor.Chain): Response {
    val mainResponse = chain.proceed(createAuthorizedRequest(chain.request()))


      // if response code is 401 or 403, network call has encountered authentication error
      if (mainResponse.code == 401 || mainResponse.code == 403) {
        if (prefs.getIsLoggedIn()){
        Coroutines.io{
          var user = appDatabase.getUserDao().getUserInfo()
          if (prefs.getIsFacebookUser()) {
            val response = client.api.facebookLogin(prefs.getFacebookAccessToken())
            if (response.isSuccessful) {
              // save new access token
              prefs.setCurrentToken(response.body()!!.token)
              user.token = response.body()!!.token
              user.expiredAt = response.body()!!.expiresOn
            }
          } else {
            val jsonObject = JSONObject()
            jsonObject.put("identifier", user.username)
            jsonObject.put("password", user.password)
            jsonObject.put("password", user.trustedDevice)
            val loginResponse = client.api.logIn(jsonObject.toString())
            if (loginResponse.isSuccessful) {
              val authResponse: AuthResponse? = loginResponse.body()
              authResponse.let {
                user = User(
                    it?.username, it?.email, it?.firstName, it?.lastName, it?.avatarURL,
                    it?.crypto, it?.twoFactorAuthentication,
                    it?.authToken?.token, it?.authToken?.expiresOn, user.password, user.trustedDevice
                )
                Timber.d(authResponse.toString())
              }
            }
          }
          appDatabase.getUserDao().replace(user)
          prefs.setIsLoggedIn(true)
          prefs.setCurrentToken(user.token)
        }
        throw SessionExpirationException("Your session was expired. Please try again")}
      }
      else
        prefs.setIsLoggedIn(true)

    return mainResponse
  }

  private fun createAuthorizedRequest(request: Request): Request {
    val bodyString = request.body.bodyToString()
    val newBody = if (!bodyString.isNullOrEmpty())
      bodyString.toRequestBody(APPLICATION_JSON.toMediaType())
    else
      request.body
    val newRequest = request.newBuilder()
        .apply {
          url(request.url)
          method(request.method, newBody)
          header(CONTENT_TYPE, APPLICATION_JSON)
          header(ACCEPT, APPLICATION_JSON)
          val token = prefs.getCurrentToken()
          if (!token.isNullOrEmpty()) {
            addHeader(AppConstants.X_AUTH_TOKEN, token)
          }
        }
    return newRequest.build()
  }

  private fun RequestBody?.bodyToString(): String {
    return try {
      val buffer = Buffer()
      if (this != null) this.writeTo(buffer) else return ""
      buffer.readUtf8()
    } catch (e: IOException) {
      ""
    }
  }

}
