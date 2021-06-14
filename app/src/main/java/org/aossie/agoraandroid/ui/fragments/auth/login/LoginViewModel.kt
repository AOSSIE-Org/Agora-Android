package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.dto.LoginDto
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val prefs: PreferenceProvider
) : ViewModel() {

  var authListener: AuthListener? = null
  var loginListener: LoginListener? = null

  fun getLoggedInUser() = userRepository.getUser()

  fun logInRequest(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    authListener?.onStarted()
    if (identifier.isEmpty() || password.isEmpty()) {
      authListener?.onFailure("Invalid Email or Password")
      return
    }
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.userLogin(LoginDto(identifier, trustedDevice ?: "", password))
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto, it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, password, trustedDevice
          )
          userRepository.saveUser(user)
          Timber.d(user.toString())
          if (!it.twoFactorAuthentication!!) {
            authListener?.onSuccess()
          } else {
            loginListener?.onTwoFactorAuthentication(password, user.crypto!!)
          }
        }
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

  fun facebookLogInRequest() {
    authListener!!.onStarted()
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.fbLogin()
        getUserData(authResponse)
        Timber.d(authResponse.toString())
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

  private fun getUserData(authResponse: AuthResponse) {
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val user = User(
          authResponse.username, authResponse.email, authResponse.firstName, authResponse.lastName,
          authResponse.avatarURL, authResponse.crypto, authResponse.twoFactorAuthentication,
          authResponse.authToken?.token, authResponse.authToken?.expiresOn
        )
        userRepository.saveUser(user)
        Timber.d(authResponse.toString())
        prefs.setIsFacebookUser(true)
        authListener?.onSuccess()
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }
}
