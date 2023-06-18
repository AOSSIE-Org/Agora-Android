package org.aossie.agoraandroid.ui.screens.common.Util

data class ScreensState(
  val isLoading: Pair<String,Boolean> = Pair("",false),
  val error: Pair<String,Boolean> = Pair("",false),
  val errorResource: Pair<Int,Boolean> = Pair(0,false),
)
