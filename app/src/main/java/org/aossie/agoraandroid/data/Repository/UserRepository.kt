package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.data.network.responses.Token
import org.json.JSONException
import org.json.JSONObject

class UserRepository(
  private val api: Api,
  private val appDatabase: AppDatabase,
  private val preferenceProvider: PreferenceProvider
) : ApiRequest() {

  suspend fun userSignup(
    identifier: String,
    password: String,
    email: String?,
    firstName: String?,
    lastName: String?,
    securityQuestion: String?,
    securityAnswer: String?
  ): String{
    val jsonObject = JSONObject()
    val securityJsonObject = JSONObject()
    try {
      jsonObject.put("identifier", identifier)
      jsonObject.put("password", password)
      jsonObject.put("email", email)
      jsonObject.put("firstName", firstName)
      jsonObject.put("lastName", lastName)
      securityJsonObject.put("question", securityQuestion)
      securityJsonObject.put("answer", securityAnswer)
      jsonObject.put("securityQuestion", securityJsonObject)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    return apiRequest { api.createUser(jsonObject.toString()) }
  }

  suspend fun userLogin(
    identifier: String,
    password: String
  ): AuthResponse {
    val jsonObject = JSONObject()
    jsonObject.put("identifier", identifier)
    jsonObject.put("password", password)
    return apiRequest { api.logIn(jsonObject.toString()) }
  }

  suspend fun fbLogin(
    accessToken: String
  ): Token {
    return apiRequest { api.facebookLogin(accessToken) }
  }

  suspend fun getUserData(
    token: String
  ): AuthResponse {
    return apiRequest { api.getUserData(token) }
  }

  suspend fun saveUser(user: User) {
    appDatabase.getUserDao().removeUser()
    appDatabase.getUserDao().insert(user)
    preferenceProvider.setIsLoggedIn(true)
    preferenceProvider.setCurrentToken(user.token)
  }

  suspend fun logout(): String {
    return apiRequest{ api.logout(preferenceProvider.getCurrentToken()) }
  }

  fun getUser(): LiveData<User>{
    return appDatabase.getUserDao().getUser()
  }

  suspend fun deleteUser(){
    appDatabase.getUserDao().removeUser()
    preferenceProvider.clearData()
    appDatabase.getElectionDao().deleteAllElections()
  }

  suspend fun sendForgotPasswordLink(username: String?): String{
    return apiRequest { api.sendForgotPassword(username) }
  }

  suspend fun updateUser(token: String, body: String): ArrayList<String>{
    return apiRequest { api.updateUser(token, body) }
  }

  suspend fun changePassword(token: String, body: String): ArrayList<String>{
    return apiRequest { api.changePassword(body, token) }
  }

}