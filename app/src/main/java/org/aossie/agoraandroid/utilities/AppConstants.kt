package org.aossie.agoraandroid.utilities

object AppConstants {
  const val ok = "OK"
  const val undo = "UNDO"
  const val FACEBOOK = "facebook"
  const val X_AUTH_TOKEN = "X-Auth-Token"
  const val ACCESS_TOKEN = "Access-Token"
  const val ACCEPT = "Accept"
  const val CONTENT_TYPE = "Content-Type"
  const val APPLICATION_JSON = "application/json"
  const val BAD_REQUEST_CODE = 400
  const val UNAUTHENTICATED_CODE = 401
  const val INVALID_CREDENTIALS_CODE = 403
  const val INTERNAL_SERVER_ERROR_CODE = 500
  const val BAD_REQUEST_MESSAGE = "Please verify your email id"
  const val UNAUTHENTICATED_MESSAGE = "Unauthenticated"
  const val INVALID_CREDENTIALS_MESSAGE = "Invalid Credentials"
  const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error. Please enter valid input"
  const val CANDIDATE_ITEM_CLICKED = 0
  const val UPVOTED_CANDIDATE_ITEM_CLICKED = 1
  const val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"
  const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"
  const val retryCount = 3
}
