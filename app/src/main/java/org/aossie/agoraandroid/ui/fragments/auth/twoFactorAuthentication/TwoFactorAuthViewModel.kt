package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class TwoFactorAuthViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
    private val prefs: PreferenceProvider
) : ViewModel() {

  var authListener: AuthListener? = null

  fun verifyOTP(
    otp: String,
    trustedDevice: Boolean,
    password: String,
    crypto: String
  ) {
    authListener?.onStarted()
    if (otp.isEmpty()) {
      authListener?.onFailure("Invalid OTP")
      return
    }
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.verifyOTP(otp, trustedDevice, crypto)
        authResponse.let {
          val user = User(
              it.username, it.email, it.firstName, it.lastName, it.crypto,
              it.twoFactorAuthentication,
              it.token?.token, it.token?.expiresOn, password, it.trustedDevice
          )
          userRepository.saveUser(user)
          Log.d("friday", user.toString())
          authListener?.onSuccess()
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

}