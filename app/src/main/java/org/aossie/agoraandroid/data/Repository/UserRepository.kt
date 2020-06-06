package org.aossie.agoraandroid.data.Repository

import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.json.JSONObject

class UserRepository(
  private val api: Api
): ApiRequest() {

  suspend fun userLogin(identifier: String, password: String): AuthResponse{
    val jsonObject = JSONObject()
    jsonObject.put( "identifier", identifier)
    jsonObject.put("password", password)
    return apiRequest { api.logIn(jsonObject.toString()) }
  }

}