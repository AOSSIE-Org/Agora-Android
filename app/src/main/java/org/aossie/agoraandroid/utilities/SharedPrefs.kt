package org.aossie.agoraandroid.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {

  private val sharedPreferences: SharedPreferences
  private val editor: SharedPreferences.Editor

  companion object {
    private const val myPrefs = "myPrefs"
    private const val userNameKey = "userName"
    private const val firstNameKey = "firstName"
    private const val lastNameKey = "lastName"
    private const val emailKey = "emailId"
    private const val tokenKey = "token"
    private const val passKey = "userPass"
    private const val ExpiresOnKey = "expiresOn"
  }

  init {
    sharedPreferences = context.getSharedPreferences(myPrefs, Context.MODE_PRIVATE)
    editor = sharedPreferences.edit()
  }

  fun saveUserName(key: String?) {
    editor.putString(userNameKey, key)
    editor.commit()
  }

  val userName: String?
    get() = sharedPreferences.getString(userNameKey, null)

  fun saveEmail(email: String?) {
    editor.putString(emailKey, email)
    editor.commit()
  }

  val email: String?
    get() = sharedPreferences.getString(emailKey, null)

  fun saveFirstName(firstName: String?) {
    editor.putString(firstNameKey, firstName)
    editor.commit()
  }

  val firstName: String?
    get() = sharedPreferences.getString(firstNameKey, null)

  fun saveLastName(lastName: String?) {
    editor.putString(lastNameKey, lastName)
    editor.commit()
  }

  val lastName: String?
    get() = sharedPreferences.getString(lastNameKey, null)

  fun saveToken(token: String?) {
    editor.putString(tokenKey, token)
    editor.commit()
  }

  val token: String?
    get() = sharedPreferences.getString(tokenKey, null)

  fun savePass(password: String?) {
    editor.putString(passKey, password)
    editor.commit()
  }

  val pass: String?
    get() = sharedPreferences.getString(passKey, null)

  fun saveTokenExpiresOn(ExpiresOn: String?) {
    editor.putString(ExpiresOnKey, ExpiresOn)
    editor.commit()
  }

  val tokenExpiresOn: String?
    get() = sharedPreferences.getString(ExpiresOnKey, null)

  fun clearLogin() {
    editor.clear()
    editor.commit()
  }

}