package org.aossie.agoraandroid.data.network.responses

sealed class ResponseResult {
  data class Success<T>(val data: T? = null) : ResponseResult()
  data class Error(val error: String? = null) : ResponseResult()
}
