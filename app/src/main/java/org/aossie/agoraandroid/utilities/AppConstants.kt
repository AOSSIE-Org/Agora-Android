package org.aossie.agoraandroid.utilities

object AppConstants {
  const val ok = "OK"
  const val undo = "UNDO"
  const val SHOW_SNACKBAR_KEY = "showSnackBar"
  const val ELECTION_ID = "electionId"
  const val FACEBOOK = "facebook"
  const val REFRESH_ACCESS_TOKEN = "refreshAccessToken"
  const val X_AUTH_TOKEN = "X-Auth-Token"
  const val X_REFRESH_TOKEN = "X-Refresh-Token"
  const val ACCESS_TOKEN = "Access-Token"
  const val AUTHORIZATION = "Authorization"
  const val KEY = "Key="
  const val ACCEPT = "Accept"
  const val CONTENT_TYPE = "Content-Type"
  const val APPLICATION_JSON = "application/json"
  const val BAD_REQUEST_CODE = 400
  const val UNAUTHENTICATED_CODE = 401
  const val INVALID_CREDENTIALS_CODE = 403
  const val NOT_FOUND_CODE = 404
  const val NOTIFICATION_DESCRIPTION = "Default Notification Channel"
  const val INTERNAL_SERVER_ERROR_CODE = 500
  const val BAD_REQUEST_MESSAGE = "Please verify your email id"
  const val UNAUTHENTICATED_MESSAGE = "Unauthenticated"
  const val INVALID_CREDENTIALS_MESSAGE = "Invalid Credentials"
  const val INVALID_OTP_MESSAGE = "Invalid OTP"
  const val LOGIN_AGAIN_MESSAGE = "Login Again"
  const val INVALID_USERNAME_MESSAGE = "Invalid Username"
  const val NOT_FOUND_MESSAGE = "Something went wrong"
  const val USER_ALREADY_FOUND_MESSAGE = "User with this username or emailId already exists"
  const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error. Please enter valid input"
  const val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"

  //Base URL for API Call
  const val BASE_URL = "https://url/api/v1"

/* Here in the above base url you have to put the hosted APIs URL, if you are locally hosting your API and testing on either
emulator or a physical device, you have to put the URL as http://10.0.2.2:9000/v1/api for accessing api on emulator and
for physical device, you have to put your computer's local IP Address, you can find that in your WiFi settings that is connected
the computer. The IPv4 address should look something like 192.168.x.xx and URL will be http://192.168.x.xx:9000/v1/api . */

  const val FCM_URL = "https://fcm.googleapis.com/"
  const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1"
  const val SECRET_KEY_SPEC_ALGORITHM = "AES"
  const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding"
  const val SALT = "QWlGNHNhMTJTQWZ2bGhpV3U="
  const val IV = "bVQzNFNhRkQ1Njc4UUFaWA=="

  enum class Status {
    PENDING,
    ACTIVE,
    FINISHED
  }

  const val SPOTLIGHT_ANIMATION_DURATION = 500L
  const val SPOTLIGHT_SCROLL_DURATION = 0L
  const val GRAPH_ANIMATION_DURATION = 1000
}
