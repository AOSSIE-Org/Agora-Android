package org.aossie.agoraandroid.networking

import org.aossie.agoraandroid.remote.APIService
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Networking(private val api: APIService) {

    fun login(username: String, userPassword: String,
              onResponse: (response: Response<String>) -> Unit,
              onFailure: (error: String) -> Unit = {}) {

        val jsonObject = JSONObject()

        try {
            jsonObject.put("identifier", username)
            jsonObject.put("password", userPassword)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        api.logIn(jsonObject.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse.invoke(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                onFailure.invoke(t.message ?: "Something went wrong")
            }

        })

    }

    fun facebookLogin(accessToken: String,
                      onResponse: (response: Response<String>) -> Unit,
                      onFailure: (error: String) -> Unit = {}) {
        api.facebookLogin(accessToken).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse.invoke(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                onFailure.invoke(t.message ?: "Something went wrong")
            }

        })
    }

    fun getUserData(authToken: String,
                    onResponse: (response: Response<String>) -> Unit,
                    onFailure: (error: String) -> Unit = {}) {
        api.getUserData(authToken).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                onResponse.invoke(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                onFailure.invoke(t.message ?: "Something went wrong")
            }

        })

    }
}