package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

class ForgotPasswordViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  lateinit var authListener: AuthListener

  fun sendResetLink(userName: String?)= Coroutines.main {
    authListener.onStarted()
    try{
      userRepository.sendForgotPasswordLink(userName)
      authListener.onSuccess()
    }catch (e : ApiException){
      if(e.message == "412"){
        authListener.onFailure("Invalid Username")
      }else{
        authListener.onFailure(e.message!!)
      }
    }catch (e : NoInternetException){
      authListener.onFailure(e.message!!)
    }catch (e : Exception){
      authListener.onFailure(e.message!!)
    }
  }

}