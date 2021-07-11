package org.aossie.agoraandroid.utilities

object AppConstants {
  const val ok = "OK"
  const val undo = "UNDO"
  const val SHOW_SNACKBAR_KEY = "showSnackBar"
  const val FACEBOOK = "facebook"
  const val REFRESH_ACCESS_TOKEN = "refreshAccessToken"
  const val X_AUTH_TOKEN = "X-Auth-Token"
  const val X_REFRESH_TOKEN = "X-Refresh-Token"
  const val ACCESS_TOKEN = "Access-Token"
  const val ACCEPT = "Accept"
  const val CONTENT_TYPE = "Content-Type"
  const val APPLICATION_JSON = "application/json"
  const val BAD_REQUEST_CODE = 400
  const val UNAUTHENTICATED_CODE = 401
  const val INVALID_CREDENTIALS_CODE = 403
  const val NOT_FOUND_CODE = 404
  const val INTERNAL_SERVER_ERROR_CODE = 500
  const val BAD_REQUEST_MESSAGE = "Please verify your email id"
  const val UNAUTHENTICATED_MESSAGE = "Unauthenticated"
  const val INVALID_CREDENTIALS_MESSAGE = "Invalid Credentials"
  const val INVALID_OTP_MESSAGE = "Invalid OTP"
  const val LOGIN_AGAIN_MESSAGE = "Login Again"
  const val INVALID_USERNAME_MESSAGE = "Invalid Username"
  const val NOT_FOUND_MESSAGE = "Something went wrong"
  const val USER_ALREADY_FOUND_MESSAGE = "User with this username already exists"
  const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error. Please enter valid input"
  const val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"
  const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"
  const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1"
  const val SECRET_KEY_SPEC_ALGORITHM = "AES"
  const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding"
  const val SALT = "QWlGNHNhMTJTQWZ2bGhpV3U="
  const val IV = "bVQzNFNhRkQ1Njc4UUFaWA=="
  enum class Status {
    PENDING, ACTIVE, FINISHED
  }
}
