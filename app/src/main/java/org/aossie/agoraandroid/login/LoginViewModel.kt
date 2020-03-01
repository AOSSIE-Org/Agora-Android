package org.aossie.agoraandroid.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.networking.Networking
import org.aossie.agoraandroid.utilities.CacheManager
import org.aossie.agoraandroid.utilities.State
import org.json.JSONException
import org.json.JSONObject

class LoginViewModel(private val networking: Networking, private val cacheManager: CacheManager) : ViewModel() {

    val loginState = MutableLiveData<State>()
    val facebookState = MutableLiveData<State>()

    init {
        loginState.value = State.Normal
        facebookState.value = State.Normal
    }

    fun logInRequest(userName: String, userPassword: String) {
        loginState.value = State.Loading
        networking.login(userName, userPassword, {
            if (it.message() == "OK") {
                try {
                    cacheManager.cacheUserCredentials(JSONObject(it.body() ?: ""), userPassword)
                    loginState.value = State.Success
                } catch (e: JSONException) {
                    e.printStackTrace()
                    loginState.value = State.Error.apply { errors.putString("error", e.message) }
                }
            } else {
                loginState.value = State.Error.apply { errors.putString("error", "Wrong User Name or Password") }
            }
        }) {
            loginState.value = State.Error.apply { errors.putString("error", "Something went wrong please try again") }
        }
    }

    fun facebookLogInRequest(accessToken: String) {
        facebookState.value = State.Loading
        networking.facebookLogin(accessToken, {
            if (it.message() == "OK") {
                try {
                    val jsonObject = JSONObject(it.body() ?: "")
                    cacheManager.cacheToken(jsonObject)
                    val authToken = jsonObject.getString("token")
                    getUserData(authToken)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    facebookState.value = State.Error.apply { errors.putString("error", e.message) }
                }
            } else {
                facebookState.value = State.Error.apply { errors.putString("error", "Wrong User Name or Password") }
            }
        }) {
            facebookState.value = State.Error.apply { errors.putString("error", "Something went wrong please try again") }
        }

    }

    private fun getUserData(authToken: String) {
        facebookState.value = State.Loading
        networking.getUserData(authToken, {
            if (it.message() == "OK") {
                try {
                    cacheManager.cacheUserData(JSONObject(it.body() ?: ""))
                    facebookState.value = State.Success
                } catch (e: JSONException) {
                    e.printStackTrace()
                    facebookState.value = State.Error.apply { errors.putString("error", e.message) }
                }
            } else {
                facebookState.value = State.Error.apply { errors.putString("error", "Wrong User Name or Password") }
            }

        }) {
            facebookState.value = State.Error.apply { errors.putString("error", "Something went wrong please try again") }
        }
    }

}