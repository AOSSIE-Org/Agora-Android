package org.aossie.agoraandroid.data.network.responses

sealed class ResponseResult {
  object Success : ResponseResult()
  data class Error(val error: String? = null) : ResponseResult()
}
