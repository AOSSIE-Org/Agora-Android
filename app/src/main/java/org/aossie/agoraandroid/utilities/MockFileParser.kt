package org.aossie.agoraandroid.utilities

import java.io.InputStreamReader

class MockFileParser(path: String) {
  val content: String

  init {
    val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
    content = reader.readText()
    reader.close()
  }
}