package org.aossie.agoraandroid.ui.fragments.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

class SignUpViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {
  private var loadToast: LoadToast? = null

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
        Log.d("friday", call)
        authListener.onSuccess()
      }catch (e: ApiException){
        if(e.message == "409"){
          authListener.onFailure("User with this username already exists")
        }else {
          authListener.onFailure(e.message!!)
        }
      }catch (e: NoInternetException){
        authListener.onFailure(e.message!!)
      }catch (e: Exception){
        authListener.onFailure(e.message!!)
      }
    }
  }

}