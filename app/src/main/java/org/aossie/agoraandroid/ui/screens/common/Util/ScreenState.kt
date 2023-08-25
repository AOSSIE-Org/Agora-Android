package org.aossie.agoraandroid.ui.screens.common.Util

data class ScreensState(
  val loading: Pair<Any,Boolean> = Pair("",false),
  val message: Pair<Any,Boolean> = Pair("",false),
)