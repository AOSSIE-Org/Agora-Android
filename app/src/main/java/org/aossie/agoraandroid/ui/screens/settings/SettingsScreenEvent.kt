package org.aossie.agoraandroid.ui.screens.settings

sealed class SettingsScreenEvent{
  data class ChangeAppLanguage(val language: Pair<String, String>):SettingsScreenEvent()
  object OnAccountSettingClick:SettingsScreenEvent()
  object OnAboutUsClick:SettingsScreenEvent()
  object OnShareWithOthersClick:SettingsScreenEvent()
  object OnContactUsClick:SettingsScreenEvent()
  object OnLogoutClick:SettingsScreenEvent()
}