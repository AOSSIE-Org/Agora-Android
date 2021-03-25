package org.aossie.agoraandroid.utilities

object AppConstants {
  const val ok = "OK"
  const val undo = "UNDO"
  const val BAD_REQUEST_CODE = 400
  const val UNAUTHENTICATED_CODE = 401
  const val INVALID_CREDENTIALS_CODE = 403
  const val INTERNAL_SERVER_ERROR_CODE = 500
  const val BAD_REQUEST_MESSAGE = "Bad Request"
  const val UNAUTHENTICATED_MESSAGE = "Unauthenticated"
  const val INVALID_CREDENTIALS_MESSAGE = "Invalid Credentials"
  const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error. Please enter valid input"
  const val CANDIDATE_ITEM_CLICKED = 0
  const val UPVOTED_CANDIDATE_ITEM_CLICKED = 1
  const val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"
}