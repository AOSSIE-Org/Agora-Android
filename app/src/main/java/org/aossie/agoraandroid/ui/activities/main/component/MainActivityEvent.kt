package org.aossie.agoraandroid.ui.activities.main.component

sealed class MainActivityEvent{
  data class ShowTitle(val title:String): MainActivityEvent()
  data class BottomNavVisibility(val isVisible:Boolean): MainActivityEvent()
  data class AppBarVisibility(val isVisible:Boolean): MainActivityEvent()
  data class BackBtnVisibility(val isVisible:Boolean): MainActivityEvent()

}
