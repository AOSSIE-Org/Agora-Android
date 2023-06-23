package org.aossie.agoraandroid.utilities

object LocaleUtil {

  fun getSupportedLanguages(): List<Pair<String, String>> {
  return  listOf(
    Pair("English","en"),
    Pair("Hindi","hi"),
  )
  }
}