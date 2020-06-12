package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.json.JSONObject

class UserRepository(
  private val api: Api,
  private val appDatabase: AppDatabase,
  private val preferenceProvider: PreferenceProvider
) : ApiRequest() {

  suspend fun userLogin(
    identifier: String,
    password: String
  ): AuthResponse {
    val jsonObject = JSONObject()
    jsonObject.put("identifier", identifier)
    jsonObject.put("password", password)
    return apiRequest { api.logIn(jsonObject.toString()) }
  }

  suspend fun saveUser(user: User) {
    appDatabase.getUserDao().insert(user)
    preferenceProvider.setIsLoggedIn(true)
    preferenceProvider.setCurrentToken(user.token)
  }

  fun getUser(): LiveData<User>{
    return appDatabase.getUserDao().getUser()
  }

  suspend fun deleteUser(){
    appDatabase.getUserDao().removeUser()
    preferenceProvider.setIsLoggedIn(false)
  }

}