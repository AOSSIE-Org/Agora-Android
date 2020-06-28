package org.aossie.agoraandroid.ui.fragments.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.Token
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val prefs: PreferenceProvider
) : ViewModel() {

  var authListener: AuthListener? = null

  fun getLoggedInUser() = userRepository.getUser()

  fun logInRequest(
    identifier: String,
    password: String
  ) {
    authListener?.onStarted()
    if (identifier.isEmpty() || password.isEmpty()) {
      authListener?.onFailure("Invalid Email or Password")
      return
    }
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.userLogin(identifier, password)
        authResponse.let {
          val user = User(
              it.username, it.email, it.firstName, it.lastName, it.towFactorAuthentication,
              it.token?.token, it.token?.expiresOn, password
          )
          userRepository.saveUser(user)
          Log.d("friday", user.toString())
          authListener?.onSuccess()
        }
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      }catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

  fun facebookLogInRequest(accessToken: String?) {
    authListener!!.onStarted()
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.fbLogin(accessToken!!)
        getUserData(authResponse)
        Log.d("friday", authResponse.toString())
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

  private fun getUserData(token: Token) {
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.getUserData(token.token!!)
        authResponse.let {
          val user = User(
              it.username, it.email, it.firstName, it.lastName, it.towFactorAuthentication,
              token.token, token.expiresOn
          )
          userRepository.saveUser(user)
          Log.d("friday", authResponse.toString())
          prefs.setIsFacebookUser(true)
          authListener?.onSuccess()
        }
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

}