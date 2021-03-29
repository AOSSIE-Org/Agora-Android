package org.aossie.agoraandroid.data.network.responses

sealed class ResponseResult {
  class Success(text: String? = null) : ResponseResult() {
    val message = text
  }
  class Error(errorText: String) : ResponseResult() {
    val message = errorText
  }
}