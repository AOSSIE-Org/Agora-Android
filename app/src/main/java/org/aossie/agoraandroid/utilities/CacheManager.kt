package org.aossie.agoraandroid.utilities

import org.json.JSONObject

class CacheManager(private val sharedPrefs: SharedPrefs) {

    fun cacheUserCredentials(jsonObject: JSONObject, userPassword: String) {
        val token = jsonObject.getJSONObject("token")
        val expiresOn = token.getString("expiresOn")
        val key = token.getString("token")
        val sUserName = jsonObject.getString("username")
        val email = jsonObject.getString("email")
        val firstName = jsonObject.getString("firstName")
        val lastName = jsonObject.getString("lastName")
        sharedPrefs.saveUserName(sUserName)
        sharedPrefs.saveEmail(email)
        sharedPrefs.saveFirstName(firstName)
        sharedPrefs.saveLastName(lastName)
        sharedPrefs.saveToken(key)
        sharedPrefs.savePass(userPassword)
        sharedPrefs.saveTokenExpiresOn(expiresOn)
    }

    fun cacheToken(jsonObject: JSONObject) {
        val expiresOn = jsonObject.getString("expiresOn")
        val authToken = jsonObject.getString("token")
        sharedPrefs.saveToken(authToken)
        sharedPrefs.saveTokenExpiresOn(expiresOn)
    }

    fun cacheUserData(jsonObject: JSONObject){
        val userName = jsonObject.getString("username")
        val email = jsonObject.getString("email")
        val firstName = jsonObject.getString("firstName")
        val lastName = jsonObject.getString("lastName")
        sharedPrefs.saveUserName(userName)
        sharedPrefs.saveEmail(email)
        sharedPrefs.saveFirstName(firstName)
        sharedPrefs.saveLastName(lastName)
    }
}