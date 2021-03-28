package org.aossie.agoraandroid.data.network.responses

sealed class ResponseResults {
  class Success(text: String? = null) : ResponseResults() {
    val message = text
  }
  class Error(errorText: String) : ResponseResults() {
    val message = errorText
  }
}