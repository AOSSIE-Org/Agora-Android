package org.aossie.agoraandroid.ui.fragments.auth.signup

import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class SignUpViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  lateinit var authListener: AuthListener
  fun signUpRequest(
    userName: String,
    userPassword: String,
    userEmail: String,
    firstName: String,
    lastName: String,
    securityQuestion: String,
    securityAnswer: String
  ) {
    authListener.onStarted()
    viewModelScope.launch(Dispatchers.Main){
      try {
        val call = userRepository.userSignup(
            userName,
            userPassword,
            userEmail,
            firstName,
            lastName,
            securityQuestion,
            securityAnswer
        )
        Timber.d(call)
        authListener.onSuccess()
      }catch (e: ApiException){
        if(e.message == "409"){
          authListener.onFailure("User with this username already exists")
        }else {
          authListener.onFailure(e.message!!)
        }
      }catch (e: SessionExpirationException){
        authListener.onSessionExpired()
      }catch (e: NoInternetException){
        authListener.onFailure(e.message!!)
      }catch (e: Exception){
        authListener.onFailure(e.message!!)
      }
    }
  }

}