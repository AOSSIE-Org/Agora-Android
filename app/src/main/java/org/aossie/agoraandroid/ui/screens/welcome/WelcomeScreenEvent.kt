package org.aossie.agoraandroid.ui.screens.welcome

sealed class WelcomeScreenEvent{
  object GET_STARTED_CLICKED: WelcomeScreenEvent()
  object ALREADY_HAVE_ACCOUNT_CLICKED: WelcomeScreenEvent()
}

